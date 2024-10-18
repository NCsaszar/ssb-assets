# Accounts/Transactions Service

Welcome to the Accounts/Transactions Service! This document provides an overview of the Gitflow workflow, a branching
strategy we use for development. Please follow these guidelines for contributing to the project.

### Branches

- **main**: The source of truth for production-ready code.
- **staging**: The staging branch is an environment that's as close to the production environment as possible. Before
  any code is merged into main and deployed to production, it's merged into staging.
- **dev**: The development branch contains pre-release code that will eventually be merged into the main branch.
- **feature/***: Feature branches are used to develop new features and are based on the dev branch.
- **hotfix/***: Hotfix branches are necessary fixes to be made directly on production code.

### Workflow Steps

#### 1. Cloning the Repository

Start by cloning the repository to your local machine:

```bash
git clone git@git1.smoothstack.com:cohorts/2023/2023_12_11_java/organizations/the-ledger-legends/accountsservice.git
cd <accounts-service>
```

#### 2. Developing a New Feature

1. **Create a Feature Branch:**

   ```bash
   git checkout dev
   git pull origin dev
   git checkout -b feature/<feature-name>
   ```

2. **Work on the feature. Commit your changes:**

   ```bash
   git add .
   git commit -m "Feature: [Story#]DJ-XXX Add your detailed commit message here"
   ```

3. **Push the feature branch to remote:**

```bash
git fetch origin
git rebase origin/dev
   ```

   ```bash
   git push -u origin feature/<feature-name>
   ```

#### 3. Creating a Pull Request

Once the feature is complete and tested:

- Push the latest changes.
- Go to the repository on GitLab.
- Create a new merge request from `feature/<feature-name>` to `dev`.
- Assign reviewers and wait for approval.

#### 4. Merging with Dev

After the feature branch has been reviewed and approved:

- Merge the feature branch into `dev`.
- Delete the feature branch if it's no longer needed.

#### 5. Merging to Staging

When `dev` has accumulated enough features and improvements for a pre-release:

1. **Merge Dev into Staging:**

   ```bash
   git checkout staging
   git pull origin staging
   git merge dev
   ```

2. **Perform all necessary tests in the staging branch to ensure everything works as expected.**
3. **Resolve any issues or bugs found during staging tests.**
4. **Once approved, create a pull request from staging to main.**

#### 6. Hotfixes

If a critical issue in main needs immediate attention:

1. **Create a Hotfix Branch:**

   ```bash
   git checkout main
   git pull origin main
   git checkout -b hotfix/<hotfix>
   ```

2. **Make the fix, commit and push the branch.**
3. **Merge the hotfix branch into both `main` and `dev`.**