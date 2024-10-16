# Split-Expense

## Overview

## Actors

1. User (Participant, Admin)
2. Group
3. System (App Engine)

## Requirements
- [ ] User can create and manage(add/edit/delete) their profile
- [ ] A user's profile should contain at least their name, phone number, and password
- [ ] User can create an expense and add other users as participants.
- [ ] There will be one owner of the expense, who can manage the expense i.e. add/update/delete participants and their part of the share.
- [ ] User can create a group and manage a group
- [ ] A group can have multiple expenses
- [ ] Any member of the group can create an expense.
- [ ] User can choose to settle a group or expense
- [ ] The System can allow users to choose between multiple expense-settling algorithms
- [ ] The system should store the activity log of every expense
- [ ] Adding comments under an expense. Only participants involved in the expense can add comments.
- [ ] Notify participants of an expense about updates in the expense.


## Class Diagram



## Open Issues
- [ ] [Add classes covering the basic requirements](https://github.com/rohitnandi12/learn-by-coding-community/issues/2)
- [ ] [Adding comments under an expense](https://github.com/rohitnandi12/learn-by-coding-community/issues/3)
- [ ] [Logging Exepense change logs - Momento pattern](https://github.com/rohitnandi12/learn-by-coding-community/issues/4)
- [ ] [Notify participants of changes in an expense. Observer? Pub-Sub? Centralized notification system?](https://github.com/rohitnandi12/learn-by-coding-community/issues/5)