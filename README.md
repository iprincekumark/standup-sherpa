# 🧑‍💻 Standup Sherpa

An AI-powered agent for daily team standups.  
Collects updates via Slack, fetches GitHub PRs, auto-schedules blockers in Google Calendar and posts daily summaries back to Slack.

---

## 🚀 Tech Stack
- Java + Spring Boot  
- Slack API (slash commands, bot)  
- GitHub API  
- Google Calendar API  
- Descope Outbound Apps (secure OAuth, no hardcoded tokens)  
- H2 / Postgres (DB)  

---

## 🔑 Setup

1. Clone repo:
   ```bash
   git clone https://github.com/<your-username>/standup-sherpa.git
   cd standup-sherpa

2. Configure environment variables (.env):
   ```bash
   DESCOPE_PROJECT_ID=<your-descope-project-id>
   DESCOPE_MANAGEMENT_KEY=<your-management-key>
   DB_URL=jdbc:h2:mem:standup
   SLACK_SIGNING_SECRET=<slack-secret>

3. Run locally:
   ```bash
   ./mvnw spring-boot:run

4. Access app at:
   ```bash
   http://localhost:8080
