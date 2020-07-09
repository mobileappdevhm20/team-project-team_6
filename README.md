# 	:receipt: :euro:	 __EasyBill__ 	 :euro:	 :receipt:
[![codecov](https://codecov.io/gh/mobileappdevhm20/team-project-team_6/branch/develop/graph/badge.svg)](https://codecov.io/gh/mobileappdevhm20/team-project-team_6)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

## EasyBill is the free and open-source app for managing all your bills.

EasyBill helps you digitize, catalog and filter your bills and thus is the next step towards a more digitized world. It uses optical character recognition to translate images of your bills into a digital format. This helps you to have a better overview on your spendings.

## Content
- [Story Board](#story-board)
- [Prototype](#prototype)
  * [Screenshots of our Prototype](#screenshots-of-our-prototype)
- [Sprint 1 - Overview](#sprint-1---overview)
  * [Issues](#issues)
  * [Important pull requests](#important-pull-requests)
  * [Conclusion](#conclusion)
- [Sprint 2 - Overview](#sprint-2---overview)
  * [UI](#ui)
  * [Issues](#issues-1)
  * [Bugs](#bugs)
  * [Important pull requests](#important-pull-requests-1)
  * [Conclusion](#conclusion-1)

## Storyboard

We used [StoryboardThat](https://www.storyboardthat.com/) to create a storyboard for our app:

![Storyboard](../media/res_img/digital-bill-highres.png "Storyboard")

## Prototype

Our prototype was designed with [Figma](https://www.figma.com/). It provides an emulated fully working demo.

__[Click me for the demo!](https://www.figma.com/file/U0r2xmu9Fyja9gI72Z8CTx/EasyBill)__

### Screenshots of the prototype
<div class="row">
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/media/res_img/1.PNG" alt="drawing" width="160" height="330"/>
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/media/res_img/2.PNG" alt="drawing" width="160" height="330"/>
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/media/res_img/3.PNG" alt="drawing" width="160" height="330"/>
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/media/res_img/4.PNG" alt="drawing" width="160" height="330"/>
<img src="https://github.com/mobileappdevhm20/team-project-team_6/blob/media/res_img/5.PNG" alt="drawing" width="160" height="330"/>
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

1. The sprint started with a backlog of 11 tasks
2. Different components contributed by different team-members
	2.1. Significant overhead in merging/keeping everything in sync
3. The implementation has two entities, three DAOs, a converter-class
   and a singleton for the database
4. The implementation constists of five fragments, one of them, the archive,
   uses a RecyclerView with an Adapter that is bound to the applications
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

## Sprint 2 - Overview

### UI

Result of sprint 2 was a complete rewrite of the application. Part of the rewrite is a new, more polished layout. You can find a small demo video of the current state of the application here:

[Demo-Video](https://github.com/mobileappdevhm20/team-project-team_6/blob/media/res_img/demo.m4v)

### Issues 

* Take a picture of a bill ([#1][i1]) 
* Saved a parsed bill ([#2][i2]) 
* Apply filters ([#5][i5]) 
* Apply ordering to the archived bills ([#6][i6]) 
* See statistics ([#7][i7]) 
* Integrate OCR into the App ([#10][i10]) 
* Apply OCR to the picture of a bill ([#13][i13]) 
* Research OCR ([#38][i38]) 
* Add quality/-branches to pipline ([#48][i48]) 

[i1]: https://github.com/mobileappdevhm20/team-project-team_6/issues/1
[i2]: https://github.com/mobileappdevhm20/team-project-team_6/issues/2
[i5]: https://github.com/mobileappdevhm20/team-project-team_6/issues/5
[i6]: https://github.com/mobileappdevhm20/team-project-team_6/issues/6
[i7]: https://github.com/mobileappdevhm20/team-project-team_6/issues/7
[i10]: https://github.com/mobileappdevhm20/team-project-team_6/issues/10
[i13]: https://github.com/mobileappdevhm20/team-project-team_6/issues/13
[i38]: https://github.com/mobileappdevhm20/team-project-team_6/issues/38
[i48]: https://github.com/mobileappdevhm20/team-project-team_6/issues/48

### Bugs

[Click here to see all bugs that were fixed](https://github.com/mobileappdevhm20/team-project-team_6/issues?q=is%3Aissue+label%3Abug)


### Important pull requests

* feature/designAndOrientation -> develop ([#43][p43]) 
* pipeline/tests -> develop ([#44][p44]) 
* quality/ui revamp -> develop ([#62][p62]) 
* feature/take photo -> develop ([#65][p65]) 
* feature/order bills new ui -> develop ([#66][p66]) 

[p43]: https://github.com/mobileappdevhm20/team-project-team_6/pull/43
[p44]: https://github.com/mobileappdevhm20/team-project-team_6/pull/44
[p62]: https://github.com/mobileappdevhm20/team-project-team_6/pull/62
[p65]: https://github.com/mobileappdevhm20/team-project-team_6/pull/65
[p66]: https://github.com/mobileappdevhm20/team-project-team_6/pull/66

### Conclusion

1. Started with a sprint backlog of 16 issues
2. Different components contributed by different team-members
	2.1. Significant overhead in merging/keeping everything in sync
3. Rewrote the whole app UI because, the prototype of Sprint 1 wasn't sustainable enough.
4. Focus was on implementing statistics and picture taking capabilties (with OCR)
5. There are Unit-tests for the database module
6. The development pipeline is done with travis-ci and integrated into
   github, enforces sane coding-standards and code-quality through 
   linter-plugin. Codecoverage gets checked by codecov
7. Work was done in git-flwo, before merging features/improvements back
   in a review was needed

	     

