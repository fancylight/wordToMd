# 概述
本项目用来实现将docx格式文档转换为markdown
## xml格式分析
- 格式
貌似是使用`<Pr>`来表示格式,例如
    - 行格式:`<w:pPr>`
    - 表格格式:`<w:tblPr>`
- 行
```xml
<!--表示一行的出现-->
<w:p>
<!--表示文字的出现-->
<w:t>开发文档</w:t>
</w:p>
```
- 表格
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
    - 代码需要当前处理cell的行号,以及行数
    