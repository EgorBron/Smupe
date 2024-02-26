# Contributing

Hello there!

Thank you for your interest in contributing to this project.

Here you can find some of basic guidelines to help you get started.

## Reporting Issues

If you find any issues with the project, please report them in the [issues](https://github.com/EgorBron/Smupe/issues) section.

I was added experimental ACRA support to the project, but now it is disabled. So, please, when you see a critical error dialog (*Out Cat the Bugcatcher found a bug...*), provide following information:

* steps to reproduce the issue
* device model and OS version
* logcat output for `net.blusutils.smupe.foss` (search in the Internet how to collect it)
* screen recording
* additional context (e. g. Internet connection, app permissions or anything you think is relevant)

## Proposing Enhancements

If you have any ideas for new features, please share them in the [issues](https://github.com/EgorBron/Smupe/issues) section.

Describe your idea in details, as clear as possible. If you can, attach screenshots, videos or real code examples for it (I will be happy to see them).

## Pull Requests (changes in code)

If you want to contribute code to the project, remember:

* split your PRs to small ones (if possible)
* if you decided to change the UI, please attach screenshots or mockups
* if you decided to change app architecture, create a *draft* PR, and, until we confirm the changes, don't turn it to normal PR
* if you want to add a new translation, also create a draft PR
* PRs with very small changes will be rejected (create issue instead)
  * small changes is: a typo fix, a comment change, a small UI change (like padding intervals), etc.
  * in issue just copy link to line(s) that should be changed in the code and add changes codeblock
* PRs that add or change code comments is allowed, but only if it useful enough
* adding new dependencies is disallowed, unless it is absolutely necessary (we will try to decide it in conversation)
* **don't propose breaking changes in PRs!!!** I mean
  * breaking API changes (so app update cannot be installed above the current version)
  * global and big UI changes (it will confuse users)
  * anything that may be harmful for the user experience (including ADS, spyware, etc.)
  * ProtoBufs messages and Room DB entities changes without defined migrations
    * never use destructive migrations
    * for ProtoBufs please use reserved fields if it really necessary
  * total translation rewrites (avoid it, but if you really need to do, please create a draft PR and then we will discuss it)

Generally, please follow these steps:

1. Fork the project
2. Repeat steps from [Build](./Building.md) until project build step (but clone your fork instead of original repo)
3. Create a new branch

    ```bash
    git checkout -b my-new-feature
    ```

4. Make changes, build and test them
5. If everything works fine, commit your changes

    ```bash
    git add <files>
    git commit -m 'Sample feature commit message'
    ```

6. Push to fork

    ```bash
    git push origin my-new-feature
    ```

7. Create a new pull request in original repository. Provide all information you think is necessary in the PR description.

## Translations

Guidelines for translations will be added soon.

But you still can help me with translations. Do it as you want, freedom is the key for now.
