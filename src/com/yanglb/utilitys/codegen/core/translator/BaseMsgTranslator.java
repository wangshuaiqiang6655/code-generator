/**
 * Copyright 2015 yanglb.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanglb.utilitys.codegen.core.translator;

import java.io.File;
import java.util.List;

import com.yanglb.utilitys.codegen.core.model.TableModel;

public class BaseMsgTranslator extends BaseTranslator<List<TableModel>> {
	
	/**
	 * 文件名，优先使用--fn参数指定的文件名，如不指定使用excel名称
	 * @return 文件名
	 */
	protected String getFileName() {
		String fileName = this.paramaModel.getOptions().get("fn");
		if (fileName != null) {
			fileName.replaceAll("\"", "");
		} else {
			fileName = this.model.get(0).getExcelFileName();
			File file = new File(fileName);
			fileName = file.getName();
			
			int index = fileName.lastIndexOf(".");
			if(index != -1) {
				fileName = fileName.substring(0, index);
			}
		}
		
		return fileName;
	}
}