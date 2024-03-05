#!/bin/bash

echo "Updating all submodules to the latest commit on their respective branches..."

# Navigate to each submodule, checkout the correct branch, and pull the latest changes
git submodule foreach '
  echo "Checking out and pulling latest for $name on branch: $(git config -f $toplevel/.gitmodules submodule.$name.branch)"
  git checkout $(git config -f $toplevel/.gitmodules submodule.$name.branch) && git pull
'

# Add any changes, including updated submodules
git add .

# Check if there are changes to commit
if [ -n "$(git status --porcelain)" ]; then
  git commit -m "Updated submodules to the latest commits on their tracked branches"
  echo "Submodules updated and committed. Please push your changes to the remote repository."
else
  echo "No updates found."
fi