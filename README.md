#ANDROID FRONTEND  
##Table of Contents (Search by ex: 1a, 2a, 5ai, etc)  
##Activities  
AddCollabActivity.java (Add collaboration screen)  
CollabDetailActivity.java (Collab detail screen [holds fragments])  
CollabListActivity.java ('Home' screen with collab feed)  
ConversationsActivity.java (List of active conversations screen)  
EditCollabActivity.java (Edit collaboration screen [holds fragments])  
EditCollabClassesActivity.java (Edit collaboration classes screen)  
EditCollabSkillsActivity.java (Edit collaboration skills screen)  
EditProfileActivity.java (Edit profile activity [holds fragments]  
LoginActivity.java (Login screen)  
MessagingActivity.java (Messaging screen)  
OtherProfileActivity.java (Viewing other member profiles screen)  
ProfileActivity.java (Viewing user's own profile screen)  
RegisterActivity.java (Register screen)  
UserClassesActivity.java (Editing user's own classes screen)  
UserSkillsActivity.java (Editing user's own skills screen)  
ViewMembersOfCollabActivity.java (Viewing members of a collaboration)  
  
##Adapters  
AutoCompleteAdapter.java (Used for autocomplete when user enters skills and classes for their profile or collabs)  
ConversationAdapter.java (Used for list of conversations)  
MessagesAdapter.java (Used for messaging)  
UserListAdapter.java (Used for editing skills and classes)  
ViewMembersAdapter.java (Used to view members of a collaboration)  
  
##Config  
GlobalConfig.java (Used for base URL, avoid typos)  
  
##Fragments  
CollabDetailFragment.java (View all collaboration details, buttons, etc.)  
EditCollabDescripFragment.java (Edit collaboration description)  
EditCollabEndFragment.java (Edit collaboration end date)  
EditCollabLocationFragment.java (Edit collaboration location)  
EditCollabSizeFragment.java (Edit collaboration size)  
EditCollabStartFragment.java (Edit collaboration start date/time)  
EditCollabTitleFragment.java (Edit collaboration title)  
EditGithubFragment.java (Edit user Github link)  
EditLinkedInFragment.java (Edit user LinkedIn link)  
EditNameFragment.java (Edit user name)  
  
##Network.loopjtasks  
###Folder: realtime  
RealtimeAsync.java (Real time messaging ASYNC)  
RMSProtocol.java (Real time messaging protocol)  
CollabModel.java (Used to build collaboration objects)  
DoClassSearch.java (Used for autocomplete class search)  
DoLogin.java (Used for network/API calls to login to the app)  
DoRegister.java (Used for network/API calls to register an account)  
DoSkillSearch.java (Used for autocomplete skill search)  
GetCollabsData.java (Used for network/API calls to retrieve and add collaboration data)  
GetUserData.java (Used for network/API calls to retrieve user data)  
JoinDropCollab.java (Used for network/API calls to join, leave, and delete collaborations)  
MessageModel.java (Used to create Message objects)  
MessagingAPI.java (Used for network/API calls for messaging)  
SetUserData.java  
UpdateCollabData.java  
  
##Utils  
GeneralTools.java (General tools used throughout the application)  
Interfaces.java (Interfaces for GetUserData.java)  

