# 	:receipt: :euro:	 __EasyBill__ 	 :euro:	 :receipt:
![Build Passing Badge](https://api.travis-ci.com/mobileappdevhm20/team-project-team_6.svg?branch=master)
[![codecov](https://codecov.io/gh/mobileappdevhm20/team-project-team_6/branch/master/graph/badge.svg)](https://codecov.io/gh/mobileappdevhm20/team-project-team_6)
[![License](https://img.shields.io/github/license/mobileappdevhm20/team-project-team_6)](/LICENSE)

## EasyBill is the free and open-source app for managing all your bills.

EasyBill helps you digitize, catalog and filter your bills and thus is the next step towards a more digitized world. It uses optical character recognition to translate images of your bills into a digital format. This helps you to have a better overview on your spendings.

## Story Board

We created our storyboard in StoryboardThat.
![Storyboard](res_img/digital-bill-highres.png "Storyboard")

## Prototype

We designed our prototype in Figma, which provides a full working demo in an emulator.

__[Click me for the demo!](https://www.figma.com/file/U0r2xmu9Fyja9gI72Z8CTx/EasyBill)__

### Screenshots of our Prototype
<div class="row">
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/develop/res_img/1.PNG" alt="drawing" width="160" height="330"/>
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/develop/res_img/2.PNG" alt="drawing" width="160" height="330"/>
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/develop/res_img/3.PNG" alt="drawing" width="160" height="330"/>
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/develop/res_img/4.PNG" alt="drawing" width="160" height="330"/>
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/develop/res_img/5.PNG" alt="drawing" width="160" height="330"/>
</div>

## Sprint 1 - Overview

### Issues 

* Access detailed bill through archive ([#3][i3]) 
* Delete bill ([#4][i4]) 
* Set up database for bills ([#8][i8]) 
* Design suitable app icon ([#9][i9]) 
* Create views according to figma design ([#11][i11]) 
* Establish view navigation ([#12][i12]) 
* Create the initial project structure ([#14][i14]) 
* Create project structure ([#15][i15]) 
* Development pipeline ([#16][i16]) 
* Create archive from database ([#17][i17]) 

[i8]: https://github.com/mobileappdevhm20/team-project-team_6/issues/8
[i12]: https://github.com/mobileappdevhm20/team-project-team_6/issues/12
[i16]: https://github.com/mobileappdevhm20/team-project-team_6/issues/16
[i14]: https://github.com/mobileappdevhm20/team-project-team_6/issues/14
[i9]: https://github.com/mobileappdevhm20/team-project-team_6/issues/9
[i11]: https://github.com/mobileappdevhm20/team-project-team_6/issues/11
[i15]: https://github.com/mobileappdevhm20/team-project-team_6/issues/15
[i4]: https://github.com/mobileappdevhm20/team-project-team_6/issues/4
[i3]: https://github.com/mobileappdevhm20/team-project-team_6/issues/3
[i17]: https://github.com/mobileappdevhm20/team-project-team_6/issues/17

### Important pull requests

* feature/database -> develop ([#18][p18]) 
* development pipeline -> develop ([#19][p19]) 
* feature/design -> develop ([#22][p22]) 
* database refactoring -> develop ([#25][p25]) 
* feature/navigation -> develop ([#26][p26]) 
* experimental/viewmodel -> develop ([#31][p31])

[p18]: https://github.com/mobileappdevhm20/team-project-team_6/issues/18
[p22]: https://github.com/mobileappdevhm20/team-project-team_6/issues/22
[p26]: https://github.com/mobileappdevhm20/team-project-team_6/issues/26
[p25]: https://github.com/mobileappdevhm20/team-project-team_6/issues/25
[p19]: https://github.com/mobileappdevhm20/team-project-team_6/issues/19
[p31]: https://github.com/mobileappdevhm20/team-project-team_6/issues/31

### Conclusion

1. Started with a sprint backlog of 11 Tasks
2. Different components contributed by different team-members
	2.1. Significant overhead in merging/keeping everything in sync
3. Implementation has two Entities, three DAOs, a converter-class
   and a Database-Singleton
4. Implementation constists of five fragments, one of them, the archive,
   uses a RecyclerView with an Adapter that's bound to the applications
   ViewModel
5. The applications ViewModel offers asynchronous (e.g. non-blocking) 
   access to the database and is available through a factory
6. There are Unit-tests for the database module
7. The development pipeline is done with travis-ci and integrated into
   github, enforces sane coding-standards and code-quality through 
   linter-plugin
8. Work was done in git-flwo, before merging features/improvements back
   in a review was needed
7. All 11 tasks have been finished
	---> Result: a navigable app that has a database filled with some
	     mock data
	     
Up next for Sprint 2: Cleanup, polishing of UI, OCR, statistics, filter..
