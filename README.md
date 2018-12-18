# Favors

#### created by theSofties

It is an application that revolves around facilitating helping out other people. This is done by crowdsourcing the principle **if I help you, you owe me**. For this we have favor tokens so it is possible to both spend and earn favors and they must not be settled immediately. Just ask for a favor and you will get the help you need and desire. After you have helped you can ask for favors. You cannot only use this if you need help, but also if you have time, want to get to meet new & interesting people or just help out.

# Motivation

We are 7 computer science and communication system students at EPFL, currently enrolled in the Software Engineering course CS-305. We are allowed to create any application of our choice that fullfill following pre-requisites:

- Split app model: firebase
- Phone sensors: GPS
- User support
- Database access
- Automated test suite: 80% code coverage
> A beautiful piece of code, even if perfect, that lacks test cases will get a minimal score on the correctness and maintainability aspects of the grading rubric, so please test thoroughly.

# Build Status

[![Build Status](https://travis-ci.org/basbeu/theSofties.svg?branch=master)](https://travis-ci.org/basbeu/theSofties)

<a href="https://codeclimate.com/github/basbeu/theSofties/maintainability"><img src="https://api.codeclimate.com/v1/badges/ca33d5d2d2e82d260e05/maintainability" /></a>

<a href="https://codeclimate.com/github/basbeu/theSofties/test_coverage"><img src="https://api.codeclimate.com/v1/badges/ca33d5d2d2e82d260e05/test_coverage" /></a>

# Features

###### Completed

- users can create accounts, login, edit their profile and reset their password
- users can see a list of favors, a detailed description of a favor and can ask for a new one and set a deadline
- there are several categories favors can be ordered upon, additionally they can be ordered based on location, most recent and searched by name or description

###### Roadmap

- users can add a profile picture and add media files to their favors
- users can see the favors on a map around them with a small pin and a tag
- users can chat about meeting up for completing an issue and receive notifications
- users can "pay" using tokens and mark a favor as completed
- users can request multiple people for a favor
- users can see a history of the favors they have asked for and completed

# Scrum

### Sprint 1

##### Scrum Master: Jeremy, presenter: Bastien

- git setup, filling backlog, authentication and rudimentary database and UI

### Sprint 2

##### Scrum Master: Nicolas, presenter: Andrea

- build a professional and concise database structure with firestore
- expand UI & profile functionality
- added basic favor capability that communicated with firestore using observables

<img src="https://mail.sanchez.at/files/favor-screen-1/favor-screen-1.png" alt="drawing" width="200"/>

### Sprint 3

##### Scrum master: Charline, presenter: Jeniffer

- favor list with UI for searching & sorting, as well as asking for a new favor
- location, distance, category, etc. are recorded alongside favor

### Sprint 4

#### Scrum master : Jeremy, presenter: Charlyn

- added tests to raise coverage from 40% to 69%
- fix behavior of back button

### Sprint 5

#### Srum master: Jeniffer, presenter: Alexander

- added location functionality, autopopulate the favor creation location, autopopulate new user location, sort by distance
- added tests
- build a professional testDatabase for firesbase testing

### Sprint 6

#### Scrum master: Andrea, presenter: Bastien

- increase code coverage to pass 80% (accomplished on November 1st) 
- send email to user to notify them of a interest in help
- purchased domain name favors.services
- made dummy webstie to replace 404 error on website access www.favors.services

# Tests

Can be run with the following command:

``` bash
./gradlew build connectedCheck jacocoTestReport
```

## API Reference

##### Database

###### Users

[Please insert the most important API functionality with description and example here for database]

```java
code example goes here
```

###### Favors

[Please insert the most important API functionality with description and example here for database]

```
code example goes here
```

###### Email Notification

This feature enables emails to be send to a specified source from anywhere in the app.

```java
import ch.epfl.sweng.favors.utils.email.EmailUtils;

 /**
     * Sends an email to `to` originating from `from`.
     *
     * @param from - the originating source of the email
     * @param to - whom the email will be send
     * @param subject - subject/header of the email
     * @param message - the main body of the email
     * @param context - the current context that is using this method
     * @param successMsg - text that will be displayed as a toast if the email is successfully send
     * @param failureMsg - text that will be displayed as a toast if the email fails to be send
     */
EmailUtils.sendEmail("SOURCE_EMAIL", "DESTINATION_EMAIL,
                    "AN AMAZING SUBJECT",
                    "MAIN BODY OF THE MESSAGE",
                    getActivity(),
                    "TEXT DISPLAYED ON SUCCESS",
                    "TEXT DISPLAYED ON FAILURE");
```

The app is using MailGun for sending the emails. Detailed logs can be seen at the following webpage: https://app.mailgun.com/
Please file a request with Jmion to be able to consult the logs in case of issues sending emails.

*source of inspiration : https://www.simplifiedcoding.net/mailgun-android-example/*


## Installation

For installing this project in the development environment you will need:

- Gradle
- Java v.1.8
- Android SDK (target: v.28, minimum: v.15)

###### Libraries

- Jacoco (testing & code coverage)

- Espresso (UI testing)

- Firebase (core, auth)

- Retrofit (email)

## Credits

This work is a project of the incredible Softies

- Alexander
- Andrea
- Bastien
- Charline
- Jeniffer
- Jeremy
- Nicolas
