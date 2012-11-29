<%--
    Document   : newPage
    Created on : 2012-9-4, 9:49:08
    Author     : sceliay & fanTasy
--%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>创建新页面</title>
        <link href="../css/newPage.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
        <script src="../SpryAssets/SpryTabbedPanels.js" type="text/javascript"></script>
        <script src="../js/jquery-1.8.1.min.js" type="text/javascript"></script>
        <link href="../SpryAssets/SpryTabbedPanels.css" rel="stylesheet" type="text/css" />
        <link href="../css/global.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
	<script type="text/javascript">
        $(document).ready(function()
        {
            //slides the element with class "menu_body" when mouse is over the paragraph
            $("#secondpane p.menu_head").mouseover(function()
            {
                 $(this).css({backgroundImage:"url(../images/down.png)"}).next("div.menu_body").slideDown(500).siblings("div.menu_body").slideUp("slow");
                 $(this).siblings().css({backgroundImage:"url(../images/left.png)"});
            });
        });
        </script>
    </head>
    <body>
        <jsp:include page = "./sidebar.jsp" flush = "true"/>
        <div class="content">
        <jsp:include page = "./head.jsp" flush="true"/>
            <div class="panel">
                <p>创建页面</p>
                    <div class="edit">
                        <form name="postPage" action="./page" method="post">
                            <input type="hidden" name="op" value="post"/>
                            <div class="edit-head">
                            <p>
                                <label>页面标题</label>
                                <input type="text" name="title" class="form-input"/>
                            </p>
                            </div>
                            <textarea id="editor1" cols="20" rows="10" name="content" class="ckeditor"></textarea>
                            <p><input style="margin-left: 5px;" type="submit" name="publish" value="发表" id="button-primary"/></p>
                        </form>
                    </div>
            </div>
            <div class="foot">
                感谢使用<a href="#">Jelly-Jolly</a> | <a href="#">反馈</a>
            </div>
                    <script type="text/javascript">
			//<![CDATA[

			// Replace the <textarea id="editor"> with an CKEditor
			// instance, using the "bbcode" plugin, shaping some of the
			// editor configuration to fit BBCode environment.
               CKEDITOR.replace( 'editor1',
				{
					extraPlugins : 'bbcode',
					// Remove unused plugins.
					removePlugins : 'bidi,button,dialogadvtab,div,filebrowser,flash,format,forms,horizontalrule,iframe,indent,liststyle,pagebreak,showborders,stylescombo,table,tabletools,templates',
					// Width and height are not supported in the BBCode format, so object resizing is disabled.
					disableObjectResizing : true,
					// Define font sizes in percent values.
					fontSize_sizes : "30/30%;50/50%;100/100%;120/120%;150/150%;200/200%;300/300%",
					toolbar :
					[
						['Source', '-', 'Save','NewPage','-','Undo','Redo'],
						['Find','Replace','-','SelectAll','RemoveFormat'],
						['Link', 'Unlink', 'Image', 'Smiley','SpecialChar'],
						'/',
						['Bold', 'Italic','Underline'],
						['FontSize'],
						['TextColor'],
						['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
						['NumberedList','BulletedList','-','Blockquote'],
						['Maximize']
					],
					// Strip CKEditor smileys to those commonly used in BBCode.
					smiley_images :
					[
						'regular_smile.gif','sad_smile.gif','wink_smile.gif','teeth_smile.gif','tounge_smile.gif',
						'embaressed_smile.gif','omg_smile.gif','whatchutalkingabout_smile.gif','angel_smile.gif','shades_smile.gif',
						'cry_smile.gif','kiss.gif'
					],
					smiley_descriptions :
					[
						'smiley', 'sad', 'wink', 'laugh', 'cheeky', 'blush', 'surprise',
						'indecision', 'angel', 'cool', 'crying', 'kiss'
					]
			} );

			//]]>
            </script>
        </div>
        <script type="text/javascript">
            var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
        </script>
    </body>
</html>
