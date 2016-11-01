# Monotweety

[![wercker status](https://app.wercker.com/status/438f38b6c6b44b4bf2454413a92c0c03/s/master "wercker status")](https://app.wercker.com/project/byKey/438f38b6c6b44b4bf2454413a92c0c03)
[![codebeat badge](https://codebeat.co/badges/b6b5eaf8-d43c-4cd7-bb74-344a982e2750)](https://codebeat.co/projects/github-com-yshrsmz-monotweety)
[![codecov](https://codecov.io/gh/yshrsmz/monotweety/branch/master/graph/badge.svg)](https://codecov.io/gh/yshrsmz/monotweety)


![header](./assets/header.png)

Simple Twitter Client just for tweeting.

<a href='https://play.google.com/store/apps/details?id=net.yslibrary.monotweety&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height="80"/></a>

## Overview

Monotweety is Twitter client app which specializes in tweeting right from your device's notification area.

When you tap the notification, editor dialog will be launched, and you can tweet from there.

If your device is Android 7.0 or up, you can literally tweet from your notification area.

Features include:

- tweet from notification area
- chain tweets as a thread 

## Architecture

Monotweety uses following approach/libraries

- [Kotlin](https://kotlinlang.org/)
- Reactive MVVM-like architecture
- [Dagger2](https://github.com/google/dagger) for Dependency Injection
- [Conductor](https://github.com/bluelinelabs/Conductor) for building View-based application
- [RxJava](https://github.com/reactivex/rxjava) for async/reactive programming

## Screenshots

|splash|setting|notification|editor
|---|---|---|---|
![](./assets/screenshots/screenshot_splash.png)|![](./assets/screenshots/screenshot_setting.png)|![](./assets/screenshots/screenshot_notification_2.png)|![](./assets/screenshots/screenshot_editor_1.png)

TBD



## How to build

- Fill `secrets.properties.template` and rename to `secrets.properties`.
- Create Firebase project and download `google-services.json`, which should have two properties; `net.yslibrary.monotweety` and `net.yslibrary.monotweety.debug`.

TBD
