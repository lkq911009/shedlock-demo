#!/bin/bash

echo "GitHub Repository Push Script"
echo "============================="
echo ""

# Check if repository URL is provided
if [ $# -eq 0 ]; then
    echo "Usage: ./push-to-github.sh <repository-url>"
    echo ""
    echo "Example:"
    echo "  ./push-to-github.sh https://github.com/yourusername/shedlock-demo.git"
    echo "  ./push-to-github.sh git@github.com:yourusername/shedlock-demo.git"
    echo ""
    echo "Steps to get the repository URL:"
    echo "1. Go to GitHub.com and create a new repository"
    echo "2. Don't initialize with README, .gitignore, or license"
    echo "3. Copy the repository URL from the setup page"
    exit 1
fi

REPO_URL=$1

echo "Repository URL: $REPO_URL"
echo ""

# Add remote origin
echo "Adding remote origin..."
git remote add origin "$REPO_URL"

if [ $? -eq 0 ]; then
    echo "✅ Remote origin added successfully"
else
    echo "❌ Failed to add remote origin"
    echo "The repository might already have a remote origin configured."
    echo "You can check with: git remote -v"
    exit 1
fi

echo ""

# Push to GitHub
echo "Pushing to GitHub..."
git push -u origin main

if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 Success! Your project has been pushed to GitHub."
    echo ""
    echo "You can now:"
    echo "- View your repository at: $REPO_URL"
    echo "- Clone it on other machines"
    echo "- Share it with others"
    echo "- Set up CI/CD pipelines"
else
    echo ""
    echo "❌ Failed to push to GitHub."
    echo "Please check:"
    echo "- Your GitHub credentials are configured"
    echo "- You have write access to the repository"
    echo "- The repository URL is correct"
fi 