# 概述
本项目用来实现将docx格式文档转换为markdown
## xml格式分析
### 格式
貌似是使用`<Pr>`来表示格式,例如
    - 行格式:`<w:pPr>`
    - 表格格式:`<w:tblPr>`
#### 行
```xml
<!--表示段落的出现-->
<w:p>
<!--表示文字的出现-->
<w:t>开发文档</w:t>
</w:p>
```
#### 表格
```xml
<!--表示表格的出现-->
<w:tbl>
<!--描述该表格列的集合,能够说明该表格有多少列-->
<w:tblGrid>
<!--列描述-->
 <w:gridCol>
 
</w:gridCol>
</w:tblGrid>
<!--表示行的出现-->
<tr>
<!--表示单元格的出现 -->
<tc>
<!--该标签来描述单元的合并,等信息-->
 <w:tcPr>
<!--vMerge:表示垂直合并,即行合并,w:val=restart表示从第一行开始合并-->
 <w:vMerge w:val="restart"/>
</w:tcPr>
</tc>
<tc>
<!--该标签表示该cell是存在列合并-->
 <w:gridSpan w:val="2"/>
</tc>
</tr>
</w:tbl>
```
 #### 标题
 尝试分析`word/styles.xml`文件,实际内部是按照`w:style`元素表达的word内部各元素
```
<w:style w:type="paragraph" w:default="1" w:styleId="1">
        <w:name w:val="Normal"/>
        <w:qFormat/>
        <w:uiPriority w:val="0"/>
        <w:pPr>
            <w:widowControl w:val="0"/>
            <w:jc w:val="both"/>
        </w:pPr>
        <w:rPr>
            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:eastAsia="楷体" w:cs="Times New Roman"/>
            <w:kern w:val="2"/>
            <w:sz w:val="28"/>
            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
        </w:rPr>
    </w:style>
 <w:style w:type="paragraph" w:styleId="2">
        <w:name w:val="heading 1"/>
        <w:basedOn w:val="1"/>
        <w:next w:val="1"/>
        <w:link w:val="29"/>
        <w:qFormat/>
        <w:uiPriority w:val="0"/>
        <w:pPr>
            <w:keepNext/>
            <w:keepLines/>
            <w:numPr>
                <w:ilvl w:val="0"/>
                <w:numId w:val="1"/>
            </w:numPr>
            <w:spacing w:before="340" w:after="330" w:line="578" w:lineRule="auto"/>
            <w:outlineLvl w:val="0"/>
        </w:pPr>
        <w:rPr>
            <w:b/>
            <w:bCs/>
            <w:kern w:val="44"/>
            <w:sz w:val="44"/>
            <w:szCs w:val="44"/>
        </w:rPr>
    </w:style>
```
 如上所示`w:style w:type` 表示该style类型,`w:styleId`表示唯一id,该id会被`document.xml`中引用,如id 2就表示一级标题
 #### 图片
 `r:embed`是`document.xml`中用来引用图片的标志,`/_rels/document.xml.rels`则记录了图片位置