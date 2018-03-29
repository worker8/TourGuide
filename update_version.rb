@version_name_regex = /VERSION_NAME=(.*)/
@version_code_regex = /VERSION_CODE=(.*)/
@file_name = "gradle.properties"

def update_version_name
  
  text = File.read(@file_name)
  
  versionName = @version_name_regex.match(text)[0].split('=')[1]
  # 2.0.0-SNAPSHOT
  
  versionNumber = versionName.split('-')[0]
  # 2.0.0 
  
  (major, minor, patch) = versionNumber.split(".")
  
  major = (major.to_i + 0).to_s
  minor = (minor.to_i + 0).to_s
  patch = (patch.to_i + 1).to_s
  
  newVersionNumber = [major, minor, patch].join(".")
  # 2.0.1
  
  newVersionName = "#{newVersionNumber}-SNAPSHOT"
  # 2.0.1-SNAPSHOT
 
  # VERSION_NAME=2.0.0-SNAPSHOT
  new_content = text.gsub(@version_name_regex, "VERSION_NAME=#{newVersionName}")
  # VERSION_NAME=2.0.1-SNAPSHOT
  
  File.open(@file_name, "w") {|file| file.puts new_content }
end

def update_version_code
  text = File.read(@file_name)
  versionCode = @version_code_regex.match(text)[0].split('=')[1]
  # 20

  newVersionCode = (versionCode.to_i + 1).to_s
  # 21

  # VERSION_CODE=20
  new_content = text.gsub(@version_code_regex, "VERSION_CODE=#{newVersionCode}")
  # VERSION_CODE=21

  File.open(@file_name, "w") {|file| file.puts new_content }
end

update_version_name
update_version_code
