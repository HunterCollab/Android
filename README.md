# FrontEnd

HOW TO WORK ON THIS REPO: (replace name with you name)


First, clone the repo using the following command "git clone https://github.com/csci49900Gather/FrontEnd.git"

Now type the command "git status", it should say you are on branch master. DO NOT GIT PUSH OR COMMIT ON THE MASTER BRANCH!

Branch off the master branch by typing "git branch nameBranch"

Now type "git status", it should still say that you are on the master branch. To start working on your branch, you need to type "git checkout nameBranch"
  
Now type "git status", it should say that you are on the nameBranch.
  
  
  
You can commit all changes using the command "git commit --all --message "Commit message""
That only does a local commit.
To push your changes to the actual github server, type "git push -u origin nameBranch" BUT you DON'T need to do it, just keep your branch local to your machine.

DO NOT PUSH TO MASTER, DO NOT WORK ON THE MASTER, DO NOT COMMIT TO MASTER!!!


-------------------------------------------------------------------------------------------------------------------
Please consult Ram/Edwin before doing any of the following if it's your first time because it might screw things up:

If you finished a feature and tested it completely, you can do the following to merge update the master branch:

First, update all your master branch to the latest version the remote server has by running "git remote update", "git checkout master", "git pull".

Now, use "git checkout nameBranch" to switch to your branch and while on your branch, run the command "git rebase master" --> 

This will "rebase" your branch. i.e. get the lastest version of the master branch and re-add all your changes onto it. You will have code conflicts after this, so you have to fix them and test the app again and make sure its working before doing the following:

So now your branch is rebased to the latest master branch code and you fixed all the conflicts and after testing, everything is working.
  
You can finally run "git checkout master" to change to the master branch and then run "git merge nameBranch" on the master branch to merge the code. Once that's done, run "git push -u origin master" to publish the changes to the remote server.

https://hackernoon.com/git-merge-vs-rebase-whats-the-diff-76413c117333
