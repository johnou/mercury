Script Runner Lite
=======

ScriptRunner Lite for JIRA allows you to extend the functionality of JIRA, executing scripts to interact with JIRA as Post Functions. Scripts can take actions such as updating an issue during a transition.

Getting started
---------------

### Development Setup

It is recommended to test your scripts on a development or staging environment before deploying them to production. To set up your own development environment you may follow the <a href="https://developer.atlassian.com/static/connect/docs/latest/guides/development-setup.html" target="_blank">Atlassian Development Setup</a> document.

### Installation

1. Log into your JIRA instance as an admin.
2. Click the admin dropdown and choose **Atlassian Marketplace**.
   * The Manage add-ons screen loads.
3. Click **Find new add-ons** from the left-hand side of the page.
4. Locate **ScriptRunner Lite for JIRA** via search.
   * The appropriate add-on version appears in the search results.
5. Click **Try free** to begin a new trial or **Buy now** to purchase a license for **ScriptRunner Lite for JIRA**.
   * You're prompted to log into MyAtlassian. ScriptRunner Lite for JIRA begins to download.
6. Enter your information and click **Generate license** when redirected to MyAtlassian.
7. Click **Apply license**.
   * If you're using an older version of UPM, you can copy and paste the license into your JIRA instance.

### Adding a script post function

To add a script post function to a transition, edit the workflow that contains the transition, select the transition, then click **Post functions** in the properties panel for the transition.

On the Post functions tab, you can see any post functions that have already been set. When you click Add post function you can choose **Script post function** from the list, and set the path of the script.

For more information see the <a href="https://confluence.atlassian.com/display/JIRA/Advanced+Workflow+Configuration#AdvancedWorkflowConfiguration-Postfunctions" target="_blank">configuring post functions</a> document.

Example
-------

### Set the issue fix version (integrating Gitlab with JIRA) 

ScriptRunner Lite provides you with the ability to set the fix version automatically when <a href="https://docs.gitlab.com/ee/project_services/jira.html#jira-issue-closing-example" target="_blank">closing issues with merge requests</a>. In this example NEXTBUILD may then be used to search and bulk update issues to the real fix version when your RC build has been promoted. *Depending on how your workflow is configured you may be required to <a href="https://confluence.atlassian.com/jira/allow-editing-of-closed-issues-138704.html" target="_blank">allow edits to issues when closed</a>.*

```groovy
import com.atlassian.jira.component.ComponentAccessor
def version = [ComponentAccessor.getVersionManager().getVersion(issue.getProjectObject().id, 'NEXTBUILD')]
issue.setFixVersions(version)
```
