#!/usr/bin/env sh

# Copyright 2015-2020 yanglb.com
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

set -e

# check require
type java >/dev/null 2>&1 || { 
  echo >&2 "`tput setaf 1`ERROR: This script require java but it's not installed. `tput sgr0`"
  echo >&2
  echo >&2 "Please install java first."
  echo >&2 "`tput setaf 4`https://www.java.com`tput sgr0`"
  exit 1
}

# Always download the latest version
# For more information about the cg.jar, please vist the following page:
# https://github.com/excel-code-generator/code-generator

repo="excel-code-generator/code-generator"
repoPath="https://github.com/$repo"
rawPath="https://raw.githubusercontent.com/$repo"

# latest version
echo "Get the latest version"
tag=`curl --silent "$repoPath/releases/latest" | sed 's#.*tag/\(.*\)".*#\1#'`
echo "Latest version: $tag"

# download
download_file() {
    echo "Download file `tput setaf 4`$2`tput sgr0`"
    curl -Lfo /tmp/$1 $2
}
echo "Downloading..."
download_file cg "$rawPath/$tag/cg"
download_file cg.jar "$repoPath/releases/download/$tag/cg.jar"

echo "Download success."

# move file
cgDir=/usr/local/lib/cg/bin
sudo mkdir -p $cgDir

sudo mv /tmp/cg.jar $cgDir/
sudo mv /tmp/cg $cgDir/

sudo chmod +x $cgDir/cg
sudo ln -bs $cgDir/cg /usr/local/bin/cg

echo "Install success!"
