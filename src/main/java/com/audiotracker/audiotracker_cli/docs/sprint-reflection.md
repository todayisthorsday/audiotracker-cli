# Sprint Reflection

## Overview
This sprint focused on building a full-stack Audiobook Tracker application consisting of a Java/Spring Boot API and a command-line interface (CLI) client. The goal was to implement CRUD functionality, custom endpoints, and an intuitive interface for users to interact with their audiobook data.

## What Went Well
- I followed a detailed master checklist, which helped me stay organized and hit all the required deliverables on time.
- The API is stable and fully functional — complete with data seeding, custom queries, and clean integration with MySQL.
- The CLI app went beyond basic output by displaying clear, human-readable summaries, with thoughtful formatting and even some stylistic touches to improve usability.
- I kept things clean on GitHub using well-named feature branches and frequent, focused commits.

## What Could’ve Gone Better
- API responses that worked in Postman didn’t always behave the same in the CLI, which led to extra debugging.
- There were a few mismatches between expected data structures — like missing IDs in session objects — that caused formatting issues.
- Some error messages were vague, which slowed down troubleshooting.

## What I’d Improve
- I’d test API responses inside the CLI earlier to catch field issues before formatting.
- Breaking up the CLI into smaller, more modular classes would’ve made the code easier to manage.
- Adding more detailed error handling from the start would’ve saved me time and confusion.

## Final Thoughts
Overall, I’m proud of how this sprint turned out. I built a functional, well-organized system with clean API architecture and a user-friendly CLI. It looks good, it works well, and I learned a lot about managing full-stack projects along the way.
