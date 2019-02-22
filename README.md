# FrontEnd

HOW TO WORK ON THIS REPO:


First, clone the repo using the following command "git clone https://github.com/csci49900Gather/FrontEnd.git"

Now type the command "git status", it should say you are on branch master. DO NOT GIT PUSH OR COMMIT ON THE MASTER BRANCH!

Branch off the master branch by typing "git branch <name>Branch"

Now type "git status", it should still say that you are on the master branch. To start working on your branch, you need to type "git checkout <name>Branch"
  
Now type "git status", it should say that you are on the <name>Branch.
  
  
  
You can commit all changes using the command "git commit --all --message "Commit message""
That only does a local commit.
To push your changes to the actual github server, type "git push -u origin <name>Branch" BUT you DON'T need to do it.

DO NOT PUSH TO MASTER, DO NOT WORK ON THE MASTER, DO NOT COMMIT TO MASTER!!!


-------------------------------------------------------------------------------------------------------------------
Please consult Ram/Edwin before doing any of the following if it's your first time because it might screw things up:

If you finished a feature and tested it completely, you can do the following to merge update the master branch:

First, update all your branches to what the remote server has by running "git remote update", "git checkout master", "git pull", "git checkout <name>Branch", "git pull". (MAKE SURE YOU COMMIT & PUSH YOUR LOCAL BRANCHES CHANGES BEFORE DOING THIS BECAUSE IF YOU DON'T, YOUR CODE MIGHT REVERT TO WHAT THE GITHUB SERVER HAS.")

First, while on your branch, run the command "git rebase master" --> This will "rebase" your branch. i.e. get the lastest version of the master branch and re-add all your changes onto it. You might have code conflicts after this, so you have to fix them and test the app again and make sure its working before doing the following:

So now your branch is updated to the latest master branch code and your code and everything is working.
Run "git push -u origin <name>Branch" to push all your changes to your branch.
  
Then, run "git checkout master" to change to the master branch and then run "git merge <name>Branch" on the master branch to merge the code.
  
 https://hackernoon.com/git-merge-vs-rebase-whats-the-diff-76413c117333
