// SpiderSync Search - Google-style functionality

const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const luckyBtn = document.getElementById('luckyBtn');
const suggestionsDiv = document.getElementById('suggestions');
const resultsDiv = document.getElementById('results');
const statsDiv = document.getElementById('stats');
const languageFilter = document.getElementById('languageFilter');
const mainContent = document.querySelector('.main-content');
const resultsContainer = document.getElementById('resultsContainer');
const quickLinks = document.getElementById('quickLinks');
const filtersSection = document.getElementById('filtersSection');
const logoContainer = document.querySelector('.logo-container');

let debounceTimer;

// Search on button click
searchBtn.addEventListener('click', performSearch);
luckyBtn.addEventListener('click', performLuckySearch);

// Search on Enter key
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
    }, 200);
});

// Hide suggestions when clicking outside
document.addEventListener('click', (e) => {
    if (!e.target.closest('.search-container')) {
        suggestionsDiv.classList.remove('active');
    }
});

// Quick links functionality
document.querySelectorAll('.quick-link').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const query = link.dataset.query;
        searchInput.value = query;
        performSearch();
    });
});

// Logo click to reset
logoContainer.addEventListener('click', () => {
    resetToHomepage();
});

function resetToHomepage() {
    searchInput.value = '';
    mainContent.classList.remove('has-results');
    resultsContainer.style.display = 'none';
    quickLinks.style.display = 'flex';
    filtersSection.style.display = 'none';
    resultsDiv.innerHTML = '';
    statsDiv.innerHTML = '';
}

async function performSearch() {
    const query = searchInput.value.trim();
    if (!query) return;
    
    const language = languageFilter.value;
    
    // Switch to results view
    mainContent.classList.add('has-results');
    resultsContainer.style.display = 'block';
    quickLinks.style.display = 'none';
    filtersSection.style.display = 'block';
    
    resultsDiv.innerHTML = `
        <div class="loading">
            <div class="loading-spinner"></div>
            <span>Searching...</span>
        </div>
    `;
    suggestionsDiv.classList.remove('active');
    
    try {
        const params = new URLSearchParams({ q: query });
        if (language) params.append('language', language);
        
        const startTime = performance.now();
        const response = await fetch(`/api/search?${params}`);
        const endTime = performance.now();
        
        if (!response.ok) throw new Error('Search failed');
        const data = await response.json();
        
        displayResults(data, endTime - startTime);
    } catch (error) {
        resultsDiv.innerHTML = '<div class="loading"><span>Error performing search. Please try again.</span></div>';
        console.error('Search error:', error);
    }
}

async function performLuckySearch() {
    const query = searchInput.value.trim();
    if (!query) {
        // Random search from quick links
        const queries = ['technology', 'science', 'programming', 'health', 'business'];
        searchInput.value = queries[Math.floor(Math.random() * queries.length)];
    }
    
    try {
        const params = new URLSearchParams({ q: searchInput.value, size: 1 });
        const response = await fetch(`/api/search?${params}`);
        const data = await response.json();
        
        if (data.results && data.results.length > 0) {
            window.open(data.results[0].url, '_blank');
        } else {
            performSearch();
        }
    } catch (error) {
        performSearch();
    }
}

async function fetchAutocomplete(query) {
    try {
        const response = await fetch(`/api/search/autocomplete?q=${encodeURIComponent(query)}&size=8`);
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

function displayResults(data, clientTime) {
    if (!data.results || data.results.length === 0) {
        resultsDiv.innerHTML = `
            <div style="padding: 20px 0;">
                <p style="color: var(--text-primary); margin-bottom: 8px;">No results found for "${escapeHtml(searchInput.value)}"</p>
                <p style="color: var(--text-secondary); font-size: 14px;">Try different keywords or check your spelling</p>
            </div>
        `;
        statsDiv.innerHTML = '';
        return;
    }
    
    // Display stats like Google
    const searchTime = (data.searchTimeMs / 1000).toFixed(2);
    statsDiv.innerHTML = `About ${data.totalResults.toLocaleString()} results (${searchTime} seconds)`;
    
    // Display results in Google style
    const resultsHTML = data.results.map((result, index) => `
        <div class="result-item" style="animation-delay: ${index * 0.05}s">
            <div class="result-url">
                ${escapeHtml(getDomain(result.url))}
            </div>
            <div class="result-title">
                <a href="${escapeHtml(result.url)}" target="_blank">${escapeHtml(result.title || 'Untitled')}</a>
            </div>
            <div class="result-description">
                ${escapeHtml(result.description || result.content?.substring(0, 200) || 'No description available')}
            </div>
            ${result.keywords ? `
                <div class="result-meta">
                    ${result.keywords.split(',').slice(0, 3).map(k => `<span class="meta-badge">${escapeHtml(k.trim())}</span>`).join('')}
                </div>
            ` : ''}
        </div>
    `).join('');
    
    resultsDiv.innerHTML = resultsHTML;
}

function getDomain(url) {
    try {
        return new URL(url).hostname;
    } catch {
        return url;
    }
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
            console.log(`SpiderSync: ${stats.totalDocuments} pages indexed`);
        }
    } catch (error) {
        console.log('Stats unavailable');
    }
}

// Initialize
loadStats();

// Focus search input on page load
searchInput.focus();
