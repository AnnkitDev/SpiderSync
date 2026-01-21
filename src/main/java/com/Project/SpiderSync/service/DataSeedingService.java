package com.Project.SpiderSync.service;

import com.Project.SpiderSync.entities.Page;
import com.Project.SpiderSync.repositories.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataSeedingService {

    private static final Logger log = LoggerFactory.getLogger(DataSeedingService.class);

    @Bean
    @Order(1)
    public CommandLineRunner seedData(PageRepository pageRepository, IndexingService indexingService) {
        return args -> {
            log.info("Checking for data seeding requirements...");

            List<Page> seedPages = new ArrayList<>();

            // Technology Pages
            seedPages.add(createPage(
                    "https://example.com/java-programming",
                    "Java Programming Guide",
                    "Comprehensive guide to Java programming language covering basics to advanced topics",
                    "Java is a high-level, class-based, object-oriented programming language. It is designed to have as few implementation dependencies as possible. Java applications are typically compiled to bytecode that can run on any Java virtual machine (JVM) regardless of the underlying computer architecture. The syntax of Java is similar to C and C++, but has fewer low-level facilities than either of them. Java is one of the most popular programming languages in use, particularly for client-server web applications.",
                    "java, programming, jvm, object-oriented, bytecode",
                    0.85,
                    0));

            seedPages.add(createPage(
                    "https://example.com/python-tutorial",
                    "Python Tutorial for Beginners",
                    "Learn Python programming from scratch with practical examples",
                    "Python is an interpreted, high-level, general-purpose programming language. Created by Guido van Rossum and first released in 1991, Python's design philosophy emphasizes code readability with its notable use of significant whitespace. Its language constructs and object-oriented approach aim to help programmers write clear, logical code for small and large-scale projects. Python is dynamically typed and garbage-collected. It supports multiple programming paradigms, including structured, object-oriented, and functional programming.",
                    "python, programming, tutorial, scripting, data science",
                    0.90,
                    0));

            seedPages.add(createPage(
                    "https://example.com/web-development",
                    "Modern Web Development",
                    "Complete guide to modern web development technologies and frameworks",
                    "Web development refers to the building, creating, and maintaining of websites. It includes aspects such as web design, web publishing, web programming, and database management. Modern web development involves using HTML5, CSS3, and JavaScript frameworks like React, Angular, and Vue.js. Backend development often uses Node.js, Python Django, Ruby on Rails, or Java Spring Boot. Understanding RESTful APIs, databases, and cloud deployment is essential for full-stack developers.",
                    "web development, html, css, javascript, react, angular",
                    0.88,
                    0));

            // Science Pages
            seedPages.add(createPage(
                    "https://example.com/quantum-physics",
                    "Introduction to Quantum Physics",
                    "Understanding the fundamentals of quantum mechanics and particle physics",
                    "Quantum physics is a fundamental theory in physics that provides a description of the physical properties of nature at the scale of atoms and subatomic particles. It is the foundation of all quantum physics including quantum chemistry, quantum field theory, quantum technology, and quantum information science. Classical physics, the description of physics that existed before the theory of relativity and quantum mechanics, describes many aspects of nature at an ordinary scale, while quantum mechanics explains the aspects of nature at small scales.",
                    "quantum physics, science, particles, mechanics, atoms",
                    0.75,
                    0));

            seedPages.add(createPage(
                    "https://example.com/climate-change",
                    "Climate Change and Global Warming",
                    "Scientific overview of climate change causes and effects",
                    "Climate change refers to long-term shifts in temperatures and weather patterns. These shifts may be natural, but since the 1800s, human activities have been the main driver of climate change, primarily due to the burning of fossil fuels like coal, oil, and gas. Burning fossil fuels generates greenhouse gas emissions that act like a blanket wrapped around the Earth, trapping the sun's heat and raising temperatures. The consequences include intense droughts, water scarcity, severe fires, rising sea levels, flooding, melting polar ice, catastrophic storms, and declining biodiversity.",
                    "climate change, global warming, environment, sustainability, greenhouse gases",
                    0.82,
                    0));

            // Business Pages
            seedPages.add(createPage(
                    "https://example.com/digital-marketing",
                    "Digital Marketing Strategies",
                    "Effective digital marketing techniques for modern businesses",
                    "Digital marketing encompasses all marketing efforts that use an electronic device or the internet. Businesses leverage digital channels such as search engines, social media, email, and other websites to connect with current and prospective customers. This includes SEO (Search Engine Optimization), content marketing, social media marketing, pay-per-click advertising, affiliate marketing, and email marketing. The key advantage of digital marketing is the ability to measure and track results in real-time, allowing businesses to optimize their strategies for better ROI.",
                    "digital marketing, seo, social media, advertising, business",
                    0.87,
                    0));

            seedPages.add(createPage(
                    "https://example.com/entrepreneurship",
                    "Starting Your Own Business",
                    "Essential guide for aspiring entrepreneurs and startup founders",
                    "Entrepreneurship is the process of designing, launching, and running a new business. Starting a business requires careful planning, market research, financial management, and dedication. Key steps include identifying a business opportunity, creating a business plan, securing funding, choosing a business structure, registering your business, and building a team. Successful entrepreneurs are innovative, risk-takers, and persistent. They understand their target market, adapt to changes, and continuously learn from failures and successes.",
                    "entrepreneurship, startup, business, innovation, funding",
                    0.79,
                    0));

            // Health & Lifestyle
            seedPages.add(createPage(
                    "https://example.com/healthy-living",
                    "Guide to Healthy Living",
                    "Tips and advice for maintaining a healthy lifestyle",
                    "Healthy living involves making choices that improve your physical and mental well-being. This includes eating a balanced diet rich in fruits, vegetables, whole grains, and lean proteins. Regular physical activity, such as 150 minutes of moderate exercise per week, is essential. Getting adequate sleep (7-9 hours for adults), managing stress through meditation or yoga, staying hydrated, and avoiding harmful habits like smoking and excessive alcohol consumption are crucial. Regular health check-ups and maintaining social connections also contribute to overall wellness.",
                    "health, lifestyle, wellness, fitness, nutrition",
                    0.84,
                    0));

            // AI & Future Tech
            seedPages.add(createPage(
                    "https://example.com/artificial-intelligence",
                    "The Future of Artificial Intelligence",
                    "Exploring the impact of AI on society and technology",
                    "Artificial Intelligence (AI) is the simulation of human intelligence processes by machines, especially computer systems. These processes include learning, reasoning, and self-correction. Specialized applications of AI include expert systems, natural language processing (NLP), speech recognition, and machine vision. As AI technologies continue to advance, they are becoming increasingly integrated into our daily lives, from virtual assistants to self-driving cars, promising to revolutionize industries and solve complex global challenges.",
                    "ai, artificial intelligence, future, machine learning, technology",
                    0.92,
                    0));

            // Astronomy & Space
            seedPages.add(createPage(
                    "https://example.com/space-exploration",
                    "Space Exploration: The Next Frontier",
                    "Latest missions and discoveries in astronomy and space science",
                    "Space exploration is the use of astronomy and space technology to explore outer space. While the exploration of space is carried out mainly by astronomers with telescopes, the physical exploration of space is conducted both by unmanned robotic space probes and human spaceflight. From the first moon landing to the latest Mars rovers and the James Webb Space Telescope, our understanding of the universe is expanding rapidly, revealing the mysteries of distant galaxies and the origins of life.",
                    "space, astronomy, nasa, universe, cosmic",
                    0.89,
                    0));

            // Finance & Economy
            seedPages.add(createPage(
                    "https://example.com/global-finance",
                    "Global Finance and Economy Trends",
                    "Analysis of current economic shifts and financial market trends",
                    "The global economy is a complex system of international trade, finance, and investment. Key factors influencing current trends include technological innovation, geopolitical shifts, and environmental sustainability. Understanding financial markets, currencies, and central bank policies is crucial for businesses and investors. As the world becomes more interconnected, economic events in one region can have far-reaching effects across the globe, emphasizing the need for robust financial systems and informed decision-making.",
                    "finance, economy, markets, business, investment",
                    0.86,
                    0));

            // Entertainment & Gaming
            seedPages.add(createPage(
                    "https://example.com/gaming-evolution",
                    "The Evolution of Video Gaming",
                    "From arcade classics to modern immersive VR experiences",
                    "Video gaming has evolved from simple pixelated games into a multi-billion dollar industry featuring immersive virtual realities and complex narratives. The rise of esports, cloud gaming, and independent developers has transformed how we consume and interact with digital entertainment. As technology continues to push the boundaries of graphics and gameplay, video games are increasingly recognized as a significant cultural and artistic medium, connecting millions of players worldwide through shared experiences and competitive play.",
                    "gaming, entertainment, esports, vr, technology",
                    0.83,
                    0));

            // Cybersecurity
            seedPages.add(createPage(
                    "https://example.com/cybersecurity-essentials",
                    "Cybersecurity Essentials for 2024",
                    "Protecting your digital assets in an increasingly connected world",
                    "Cybersecurity is the practice of protecting systems, networks, and programs from digital attacks. These cyberattacks are usually aimed at accessing, changing, or destroying sensitive information; extorting money from users; or interrupting normal business processes. As more devices become connected through the Internet of Things (IoT), the need for robust security measures, including encryption, multi-factor authentication, and regular software updates, has never been more critical to safeguard personal and organizational data.",
                    "cybersecurity, security, privacy, technology, protection",
                    0.88,
                    0));

            // Renewable Energy
            seedPages.add(createPage(
                    "https://example.com/renewable-energy",
                    "The Rise of Renewable Energy",
                    "Transitioning to sustainable power sources for a greener future",
                    "Renewable energy is energy that is collected from renewable resources, which are naturally replenished on a human timescale, such as sunlight, wind, rain, tides, waves, and geothermal heat. The transition from fossil fuels to clean energy is essential for mitigating the effects of climate change and ensuring long-term energy security. Technological advancements in solar panels, wind turbines, and battery storage are making renewable power increasingly affordable and accessible, driving a global shift toward a more sustainable and resilient energy landscape.",
                    "energy, renewable, sustainability, environment, green",
                    0.87,
                    0));

            // Psychology & Mental Health
            seedPages.add(createPage(
                    "https://example.com/mental-wellness",
                    "Prioritizing Mental Wellness",
                    "Understanding the importance of mental health in modern life",
                    "Mental health includes our emotional, psychological, and social well-being. It affects how we think, feel, and act. It also helps determine how we handle stress, relate to others, and make choices. In today's fast-paced and digitally connected world, prioritizing mental wellness through mindfulness, therapy, and balanced lifestyle choices is increasingly important. Raising awareness and reducing the stigma surrounding mental health issues are crucial steps toward building healthier and more supportive communities for everyone.",
                    "health, psychology, wellness, mental, therapy",
                    0.85,
                    0));

            // Gourmet Cooking
            seedPages.add(createPage(
                    "https://example.com/gourmet-cooking",
                    "Art of Gourmet Cooking",
                    "Mastering culinary techniques and exploring global flavors",
                    "Gourmet cooking is an art form that transforms simple ingredients into exquisite culinary experiences. It involves a deep understanding of flavors, textures, and presentation, as well as the mastery of specialized techniques like sous-vide, fermentation, and molecular gastronomy. Exploring global cuisines and using high-quality, seasonal ingredients allows chefs and home cooks alike to create dishes that delight the senses and bring people together through the universal language of food.",
                    "cooking, food, gourmet, culinary, lifestyle",
                    0.81,
                    0));

            // E-commerce Trends
            seedPages.add(createPage(
                    "https://example.com/ecommerce-future",
                    "Future of E-commerce and Retail",
                    "How technology is reshaping the way we shop and do business",
                    "E-commerce has revolutionized the retail industry, providing consumers with unprecedented convenience and choice. The integration of AI, augmented reality, and personalized marketing is further transforming the shopping experience, allowing brands to connect with customers in more meaningful ways. As mobile shopping and social commerce continue to grow, businesses must adapt their strategies to stay competitive in a rapidly evolving digital marketplace, focusing on seamless logistics, customer engagement, and data-driven insights.",
                    "ecommerce, business, retail, technology, shopping",
                    0.84,
                    0));

            // World History
            seedPages.add(createPage(
                    "https://example.com/ancient-civilizations",
                    "Exploring Ancient Civilizations",
                    "A journey through the history and mysteries of the past",
                    "The study of ancient civilizations provides fascinating insights into the origins of human society, culture, and innovation. From the majestic pyramids of Egypt and the philosophy of Ancient Greece to the engineering marvels of the Roman Empire and the sophisticated city-planning of the Indus Valley, these past societies have shaped the world we live in today. Uncovering their stories through archaeology and historical research helps us understand our shared heritage and the enduring legacy of human ingenuity throughout the ages.",
                    "history, science, archaeology, culture, ancient",
                    0.82,
                    0));

            // Photography Tips
            seedPages.add(createPage(
                    "https://example.com/photography-guide",
                    "Mastering Digital Photography",
                    "Tips and techniques for capturing stunning images with any camera",
                    "Photography is the art, application, and practice of creating durable images by recording light. Whether using a professional DSLR or a smartphone, understanding the fundamentals of composition, lighting, and exposure is key to capturing compelling photos. From landscape and portrait photography to street and macro shots, the digital era has made photography more accessible than ever, allowing everyone to document their lives and express their creativity through the power of visual storytelling.",
                    "photography, art, lifestyle, technology, digital",
                    0.80,
                    0));

            // Filter out already existing pages
            List<String> seedUrls = seedPages.stream().map(p -> p.getUrl()).toList();
            List<String> existingUrls = pageRepository.findByUrlIn(seedUrls).stream()
                    .map(p -> p.getUrl())
                    .toList();

            List<Page> pagesToSave = seedPages.stream()
                    .filter(p -> !existingUrls.contains(p.getUrl()))
                    .toList();

            if (!pagesToSave.isEmpty()) {
                log.info("Saving {} new pages to database...", pagesToSave.size());
                List<Page> savedPages = pageRepository.saveAll(pagesToSave);
                log.info("Successfully saved {} pages to database", savedPages.size());

                // Index new pages in Elasticsearch
                log.info("Indexing new pages in Elasticsearch...");
                indexingService.createIndex();
                indexingService.bulkIndexPages(savedPages);
            } else {
                log.info("All seed pages already exist in database.");
            }

            // Ensure Elasticsearch is populated
            try {
                if (!indexingService.indexExists() || indexingService.getTotalDocuments() == 0) {
                    log.info("Elasticsearch index is missing or empty. Populating from database...");
                    List<Page> allPages = pageRepository.findAll();
                    if (!allPages.isEmpty()) {
                        indexingService.createIndex();
                        indexingService.bulkIndexPages(allPages);
                        log.info("Successfully indexed {} total pages from database.", allPages.size());
                    }
                }
            } catch (Exception e) {
                log.error("Could not verify or populate Elasticsearch index: {}", e.getMessage());
            }

            log.info("Data seeding and indexing check completed!");
        };
    }

    private Page createPage(String url, String title, String description, String content,
            String keywords, Double pageRank, Integer crawlDepth) {
        Page page = new Page();
        page.setUrl(url);
        page.setTitle(title);
        page.setDescription(description);
        page.setContent(content);
        page.setKeywords(keywords);
        page.setLanguage("en");
        page.setPageRank(pageRank);
        page.setCrawlDepth(crawlDepth);
        page.setStatusCode(200);
        page.setResponseTime(150);
        page.setContentHash(generateHash(content));
        page.setLastCrawledAt(LocalDateTime.now());

        return page;
    }

    private String generateHash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
