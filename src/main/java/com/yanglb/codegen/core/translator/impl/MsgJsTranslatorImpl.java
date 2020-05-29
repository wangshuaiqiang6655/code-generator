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
package com.yanglb.codegen.core.translator.impl;

import java.util.Map;

import com.yanglb.codegen.core.model.TableModel;
import com.yanglb.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.StringUtil;

public class MsgJsTranslatorImpl extends BaseMsgTranslator {
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();

		this.writableModel.setExtension("js");
		this.writableModel.setFilePath("msg/js");
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();
		StringBuilder sb = this.writableModel.getData();
		String langName = "Lang";
		// TODO: paramaModel
//		if(this.paramaModel.getOptions().get("jsLangName") != null) {
//			langName = this.paramaModel.getOptions().get("jsLangName");
//		}
		sb.append("var ");
		sb.append(langName);
		sb.append(" = { \r\n");
		int rowIndex = 0;
		
		// 分组输出
		// TODO: paramaModel
		if(true /*this.paramaModel.getOptions().get("group") != null*/) {
			for(TableModel tblModel : this.model) {
				// 如果没有Sheet名或Sheet名被#注释则添加到Root中
				String sheetName = tblModel.getSheetName();
				if(sheetName==null || sheetName.equals("") || sheetName.startsWith("@")) {
					for(Map<String, String> itm : tblModel.toList()) {
						String id = itm.get("id");
						if(StringUtil.isNullOrEmpty(id)) continue;
						// 对字符串进行转换
						String value = this.convert2JsCode(itm.get(this.msgLang));
						sb.append(String.format("    %s: \"%s\", \r\n", id, value));
					}
				} else {
					sb.append(String.format("    %s: { \r\n", tblModel.getSheetName()));
					for(Map<String, String> itm : tblModel.toList()) {
						String id = itm.get("id");
						if(StringUtil.isNullOrEmpty(id)) continue;
						// 对字符串进行转换
						String value = this.convert2JsCode(itm.get(this.msgLang));
						sb.append(String.format("        %s: \"%s\", \r\n", id, value));
					}
					int idx = sb.lastIndexOf(",");
					if(idx != -1) {
						sb.deleteCharAt(idx);
					}
					
					sb.append("    },\r\n");
				}
			}
			
			int idx = sb.lastIndexOf(",");
			if(idx != -1) {
				sb.deleteCharAt(idx);
			}
		} else {
			// 合并输出
			for(TableModel tblModel : this.model) {
				sb.append(String.format("%s    // %s\r\n", (rowIndex++ == 0)?"":"\r\n", tblModel.getSheetName()));
				for(Map<String, String> itm : tblModel.toList()) {
					String id = itm.get("id");
					if(StringUtil.isNullOrEmpty(id)) continue;
					// 对字符串进行转换
					String value = this.convert2JsCode(itm.get(this.msgLang));
					sb.append(String.format("    %s: \"%s\", \r\n", id, value));
				}
			}
			
			int idx = sb.lastIndexOf(",");
			if(idx != -1) {
				sb.deleteCharAt(idx);
			}
		}
		sb.append("}; \r\n\r\n");
	}
	
	private String convert2JsCode(String value) {
		if(value == null) return null;
		
		// 先替换\r\n，防止有文档只有\r或\n 后面再替换一次
		value = value.replaceAll("\r\n", "\\\\r\\\\n");
		value = value.replaceAll("\r", "\\\\r\\\\n");
		value = value.replaceAll("\n", "\\\\r\\\\n");
		return value;
	}
}
