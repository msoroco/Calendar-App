# Personal Project - Mauricio Soroco

## Calendar Application

**What will the application do?**
 - display a monthly calendar ( one month /w ~30 days)
 - display task(s) under appropriate day
 - add/remove tasks
 - provide statistics based on tasks tag
    - time spent on tag *x*
    - number of events w/ tag *x*
    
Tags indicate what type of event is stored in the calendar.
There will be default tags such as "calls", "sports", "personal time", "work".
The idea is that the user will be able to extract interesting statistics related to 
time spent doing certain things. The user should also be able to add their own
self-made tags.

The calendar will display 31 days (or however many days are in that month). In each 
day there will be a list Tasks (events, reminders, etc.).

A Task will contain:
 - a short name (ie what the task is about)
 - From when to when (time dedicated to the task)
 - a description of the task (extra notes/details the user wants to add).
 
 **who will use it**

Anyone needing an agenda.

**Why does it interest me**
 - usefulness for organization.
 
 #
 ## User Stories
 
 As a user:
 
 Phase 1
  - I want to be able to add tasks to a day on the calendar.
  - I want to be able to categorize the tasks for organization by adding tags to each task.
  - I want to be able to know how much time I allotted for my tasks per day.
  - I want to be able to know how much time I allotted for my tasks per month.
  
   - I want to be able to use the calendar to see future dates.
   - I want to be able to see a single month and its collection of days.
   
  Phase 2
  - I want to be able to save my generated Years to a file.
  - I want to be able to load my saved Years and the Tasks in them.
  
  Phase 3
  - I want to be able to achieve all Phase 1 and Phase 2 user stories across GUI.
 
         - I want to be able to add tasks to a day on the calendar.
         - I want to be able to categorize the tasks for organization by adding tags to each task.
         - I want to be able to know how much time I allotted for my tasks per day.
         - I want to be able to know how much time I allotted for my tasks per month.
         - I want to be able to use the calendar to see future dates.
         - I want to be able to see a single month and its collection of days.
         - I want to be able to save my generated Years to a file.
         - I want to be able to load my saved Years and the Tasks in them.
          
  - I want to see an image in the GUI.
  - I want to have the option to load or save my changes to the Calendar.
  
  #
   PHASE 4:
  
  Phase 4: Task 2
  - made model.Calendar.java robust. Throws UnsupportedYearException. Added tests for cases where exception is expected
    and cases where the exception is not expected.
  
  Phase 4: Task 3
  
  summary of refactorings:
  - reduce duplication in ui with type hierarchy.
  - split ui classes into multiple classes according to single responsibility principle.
  - reduce coupling between ui and model with variation of observer pattern.
  
  Explanations
  
  Looking at the UML diagram, it is evident there is a lot of coupling with the UI classes and the model classes.
  I would want to include the observer pattern to the ui classes with Calendar as the subject, so that I don't have to
  pass so want model classes as objects in the ui classes.
  There are a few places in the ui classes where some behaviours should be split into two classes, following
  the single responsibility principle. For example in MonthProcess there are responsibilities with making the JDialog, 
  then
  for making the different consoles, and finally for adding the frame that involves a similar operation as DayProcess 
  (this similarity could perhaps also have been extracted to reduce duplication).
  I also want to include a larger type hierarchy for classes in the ui package to extract duplicate methods. 
  It is also possible that I use a map rather than a list for classes in the model class. For example a year could 
  involve a map with month names as the keys, and the corresponding month as the value. Similarly, for Months with Days 
  and for Days with Tasks.
  
 
 