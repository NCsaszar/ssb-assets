#!/bin/bash

# Display starting message
echo "Updating all submodules to the latest commit..."

# Update all submodules to the latest commit from their tracked branch
git submodule update --remote

# Check if there are any changes to commit
if [[ `git status --porcelain` ]]; then
  # Stage all changes, including submodule updates
  git add .

  # Commit the updates
  git commit -m "Updated submodules to the latest commits"

  echo "Submodules updated and changes committed."
else
  echo "No submodule updates found."
fi