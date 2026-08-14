# Git Repository Migration Plan

The goal is to remove the existing Git history from the project and push the current codebase to a new repository on GitHub.

## User Review Required

> [!WARNING]
> This operation will **permanently delete** the entire commit history of the current repository. Only the current state of the files will be preserved and pushed to the new repository.

> [!IMPORTANT]
> The project root contains a keystore file (`LBsalesApp.jks`) and a `keystore/` directory. These are currently **not** ignored by the `.gitignore` file. Pushing these to a public (or even private) repository is generally considered a security risk. I will add them to `.gitignore` unless you specifically want them included in the repository.

## Proposed Changes

### [Root Project](file:///D:/Projects/Android_Final/sales/)

#### [MODIFY] [.gitignore](file:///D:/Projects/Android_Final/sales/.gitignore)
- Add entries to ignore keystore files (`*.jks`, `*.keystore`) and the `keystore/` directory to prevent accidental leakage of signing credentials.

#### [REMOVE] `.git/` directory
- The existing `.git` folder will be deleted to remove all old history, hooks, and configuration.

#### [NEW] Git Repository Initialization
- Initialize a new Git repository.
- Add the new remote: `https://github.com/shankarm-mobile-dev/lbsalesapp.git`.
- Commit the current state as "Initial commit".
- Push to the `main` branch of the new repository.

## Verification Plan

### Manual Verification
- Verify the new repository at `https://github.com/shankarm-mobile-dev/lbsalesapp.git` contains the files.
- Check that sensitive files like `LBsalesApp.jks` and `local.properties` are NOT in the repository.
