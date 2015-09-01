1. increase version in gradle.properties
2. git push origin master
3. ./gradlew clean build uploadArchives
4. update app's build.gradle snapshot version
5. update README.md's version
6. update git tag, convention: git tag -a v1.0.11 -m "VERSION_CODE=12, VERSION_NAME=1.0.11-SNAPSHOT"
7. git push --tags

reference:
https://github.com/chrisbanes/gradle-mvn-push

update playstore steps:
1. ./production.sh
2. upload apk
3. update commit hash in playstore readme
