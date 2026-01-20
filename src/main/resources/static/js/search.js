// Search functionality
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const suggestionsDiv = document.getElementById('suggestions');
const resultsDiv = document.getElementById('results');
const statsDiv = document.getElementById('stats');
const languageFilter = document.getElementById('languageFilter');

let debounceTimer;

// Search on button click or Enter key
searchBtn.addEventListener('click', performSearch);
searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        performSearch();
    }
});

// Autocomplete on input
searchInput.addEventListener('input', (e) => {
    clearTimeout(debounceTimer);
    const query = e.target.value.trim();
    
    if (query.length < 2) {
        suggestionsDiv.classList.remove('active');
        return;
    }
    
    debounceTimer = setTimeout(() => {
        fetchAutocomplete(query);
    }, 300);
});

// Hide suggestions when clicking outside
document.addEventListener('click', (e) => {
    if (!e.target.closest('.search-container')) {
        suggestionsDiv.classList.remove('active');
    }
});

async function performSearch() {
    const query = searchInput.value.trim();
    if (!query) return;
    
    const language = languageFilter.value;
    
    resultsDiv.innerHTML = `
        <div class="loading">
            <div class="loading-spinner"></div>
            <span>Searching high-end sources</span>
        </div>
    `;
    suggestionsDiv.classList.remove('active');
    
    try {
        const params = new URLSearchParams({ q: query });
        if (language) params.append('language', language);
        
        const response = await fetch(`/api/search?${params}`);
        if (!response.ok) throw new Error('Search failed');
        const data = await response.json();
        
        displayResults(data);
    } catch (error) {
        resultsDiv.innerHTML = '<div class="loading"><span>Error performing search. Please try again.</span></div>';
        console.error('Search error:', error);
    }
}

async function fetchAutocomplete(query) {
    try {
        const response = await fetch(`/api/search/autocomplete?q=${encodeURIComponent(query)}&size=5`);
        const suggestions = await response.json();
        
        if (suggestions.length > 0) {
            displaySuggestions(suggestions);
        } else {
            suggestionsDiv.classList.remove('active');
        }
    } catch (error) {
        console.error('Autocomplete error:', error);
    }
}

function displaySuggestions(suggestions) {
    suggestionsDiv.innerHTML = suggestions
        .map(s => `<div class="suggestion-item" onclick="selectSuggestion('${escapeHtml(s)}')">${escapeHtml(s)}</div>`)
        .join('');
    suggestionsDiv.classList.add('active');
}

function selectSuggestion(suggestion) {
    searchInput.value = suggestion;
    suggestionsDiv.classList.remove('active');
    performSearch();
}

function displayResults(data) {
    if (!data.results || data.results.length === 0) {
        resultsDiv.innerHTML = '<div class="loading">No results found. Try a different query.</div>';
        return;
    }
    
    const resultsHTML = data.results.map(result => `
        <div class="result-item">
            <div class="result-title">
                <a href="${escapeHtml(result.url)}" target="_blank">${escapeHtml(result.title)}</a>
            </div>
            <div class="result-url">${escapeHtml(result.url)}</div>
            <div class="result-description">${escapeHtml(result.description || 'No description available')}</div>
            <div class="result-meta">
                ${result.language ? `<span class="meta-badge">📝 ${result.language.toUpperCase()}</span>` : ''}
                ${result.pageRank ? `<span class="meta-badge">⭐ Rank: ${result.pageRank.toFixed(2)}</span>` : ''}
                ${result.crawlDepth !== undefined ? `<span class="meta-badge">🔍 Depth: ${result.crawlDepth}</span>` : ''}
            </div>
        </div>
    `).join('');
    
    resultsDiv.innerHTML = resultsHTML;
    
    // Display stats
    statsDiv.innerHTML = `
        <p>Found ${data.results.length} results in ${data.searchTimeMs}ms | 
        Total indexed pages: ${data.totalResults.toLocaleString()}</p>
    `;
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Load stats on page load
async function loadStats() {
    try {
        const response = await fetch('/api/search/stats');
        const stats = await response.json();
        
        if (stats.totalDocuments > 0) {
            statsDiv.innerHTML = `
                <p>📊 ${stats.totalDocuments.toLocaleString()} pages indexed and ready to search</p>
            `;
        }
    } catch (error) {
        console.error('Stats error:', error);
    }
}

loadStats();
