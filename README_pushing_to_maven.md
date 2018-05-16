update versions

1. run `ruby update_version.rb`
   - this will increase the versionName & versionCode in gradle.properties
2. update gh-pages branch install guide version

4. git push origin master
5. ./gradlew clean build uploadArchives
6. update git tag, convention: git tag -a v1.0.11 -m "VERSION_CODE=12, VERSION_NAME=1.0.11-SNAPSHOT"
7. git push --tags

reference:
https://github.com/chrisbanes/gradle-mvn-push

update playstore steps:
1. ./production.sh
2. upload apk
3. update commit hash in playstore readme
