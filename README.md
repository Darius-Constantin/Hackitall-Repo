# IntelliJ CogniFlow Plugin

**CogniFlow ** is an IntelliJ IDEA plugin that enables developers within a Git project team to ask
technical questions about code, collaborate efficiently, and receive real-time notifications.
It seamlessly integrates with GitHub Issues, OpenAI for code analysis, and a Flask-based team
socket server for notification dispatch. While productivity is important, so is wellbeing, so a 
suite of settings allows developers to take care of themselves.

---

## Organizational Features

### Annotations
- Git-synchronized virtual annotations that facilitate non-invasive extra documentation for internal
  use among a team project
- #### GPT-Driven Code Analysis
  - Select code and ask questions about its purpose or quirks.
  - The plugin sends the code to an OpenAI model (`gpt-3.5-turbo`) and receives informal explanations.
  - Implementation of used methods is also considered in the prompt for better context.

### Swift Question Distribution System
- Trigger an in-IDE dialog from selected code to create a GitHub issue with a question regarding 
  the selected context.
- The dialog lets users enter a title, description, and urgency level.
- Validates that the current Git project is:
    - Inside a Git repo
    - On a branch that is up to date with remote
- IDE-connected team members are subtly notified through Intellij of the question, which they 
  are free to answer or to continue focusing and working.

### GitHub Integration
- Issues are created via GitHub REST API (`/repos/:owner/:repo/issues`).
- Issues include metadata like title, description, and urgency.
- Users are authenticated via GitHub tokens (provided by plugin settings) and stored securely.
- Questions can be searched for from inside the IDE by context similarity to selected code.

### Real-time Team Notifications
- A basic server broadcasts team notifications (Python Flask + Socket.IO) for async, real-time 
  notifications.
- Users register by `teamID` on plugin startup, so only relevant team members receive the 
  notification.
- The plugin shows in-IDE notifications (via IntelliJ’s `NotificationGroupManager`).

## Wellbeing Features
- The plugin occasionally questionnaires the user for a "mood level," encouraging 
  self-refection. The entries are stored locally, and the plugin generates daily mood graph for 
  easy tracking
- Work-break timers can be set up from the plugin's settings for automatic reminders that taking 
  breaks is necessary for an unstressed mind!
- Detection of idle and working times. Long idle times may indicate a difficult problem, which 
  will trigger a remainder that asking for help is good practice, and suggesting 
  GPT-generated suggestions directly inside the IDE, taking into consideration the whole file.

### Plugin Settings
- Configurable fields in the Settings tab:
    - `GitHub Token`
    - `Server IP`
    - `Server Port`
    - `Team ID`
- `JBTextField`s are restricted to numeric input where appropriate.
- `Apply` button is activated only when field modifications are detected.

---

## Technical Details

### Git & Project Handling
- Git data is extracted using native `git` CLI commands (e.g., `git remote get-url origin`).
- Owner/repo info is parsed from origin URL for GitHub API integration.

### Notification Socket Client
- `NotificationSocketService` is an IntelliJ Project-level `@Service`.
- Connects to the Flask server and joins `teamID`-based rooms.
- Automatically initialized on plugin load.

### Flask Server (Python Backend)
- Endpoints:
    - `POST /broadcast`: emits messages to team-specific rooms.
- Socket Events:
    - `register`: user joins a `teamID` room.
    - `notification`: team-wide event, excluding sender (`include_self=False`).
- Input validation ensures `teamID` and `urgency` are present.

### UI/UX
- JetBrains' guidelines for UI and UX have been conscientiously and continuously considered.
- The UI is intuitive, for a smooth transition to this new, better workflow.
