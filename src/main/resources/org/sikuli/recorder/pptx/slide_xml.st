slide_xml(slide) ::= <<

<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<p:sld xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main">
	<p:cSld>
		<p:spTree>
			<p:nvGrpSpPr>
				<p:cNvPr id="1" name=""/>
				<p:cNvGrpSpPr/>
				<p:nvPr/>
			</p:nvGrpSpPr>
			<p:grpSpPr>
				<a:xfrm>
					<a:off x="0" y="0"/>
					<a:ext cx="0" cy="0"/>
					<a:chOff x="0" y="0"/>
					<a:chExt cx="0" cy="0"/>
				</a:xfrm>
			</p:grpSpPr>
			<p:pic>
				<p:nvPicPr>
					<p:cNvPr id="4" name="Picture 3" descr="Screen Shot 2013-06-06 at 8.55.09 PM.png"/>
					<p:cNvPicPr>
						<a:picLocks noChangeAspect="1"/>
					</p:cNvPicPr>
					<p:nvPr/>
				</p:nvPicPr>
				<p:blipFill>
					<a:blip r:embed="rId2">
						<a:extLst>
							<a:ext uri="{28A0092B-C50C-407E-A947-70E740481C1C}">
								<a14:useLocalDpi xmlns:a14="http://schemas.microsoft.com/office/drawing/2010/main" val="0"/>
							</a:ext>
						</a:extLst>
					</a:blip>
					<a:stretch>
						<a:fillRect/>
					</a:stretch>
				</p:blipFill>
				<p:spPr>
					<a:xfrm>
						<a:off x="0" y="0"/>
						<a:ext cx="$slide.imageCx$" cy="$slide.imageCy$"/>
					</a:xfrm>
					<a:prstGeom prst="rect">
						<a:avLst/>
					</a:prstGeom>
				</p:spPr>
			</p:pic>
			<p:sp>
				<p:nvSpPr>
					<p:cNvPr id="2" name="Rectangle 1"/>
					<p:cNvSpPr/>
					<p:nvPr/>
				</p:nvSpPr>
				<p:spPr>
					<a:xfrm>
						<a:off x="$slide.box.x$" y="$slide.box.y$"/>
						<a:ext cx="$slide.box.cx$" cy="$slide.box.cy$"/>
					</a:xfrm>
					<a:prstGeom prst="rect">
						<a:avLst/>
					</a:prstGeom>
					<a:noFill/>
					<a:ln w="38100" cmpd="sng">
						<a:solidFill>
							<a:srgbClr val="FF0000"/>
						</a:solidFill>
					</a:ln>
				</p:spPr>
				<p:style>
					<a:lnRef idx="1">
						<a:schemeClr val="accent1"/>
					</a:lnRef>
					<a:fillRef idx="3">
						<a:schemeClr val="accent1"/>
					</a:fillRef>
					<a:effectRef idx="2">
						<a:schemeClr val="accent1"/>
					</a:effectRef>
					<a:fontRef idx="minor">
						<a:schemeClr val="lt1"/>
					</a:fontRef>
				</p:style>
				<p:txBody>
					<a:bodyPr rtlCol="0" anchor="ctr"/>
					<a:lstStyle/>
					<a:p>
						<a:pPr algn="ctr"/>
						<a:endParaRPr lang="en-US"/>
					</a:p>
				</p:txBody>
			</p:sp>
			<p:sp>
				<p:nvSpPr>
					<p:cNvPr id="5" name="TextBox 1"/>
					<p:cNvSpPr txBox="1"/>
					<p:nvPr/>
				</p:nvSpPr>
				<p:spPr>
					<a:xfrm>
						<a:off x="152865" y="211649"/>
						<a:ext cx="1305955" cy="369332"/>
					</a:xfrm>
					<a:prstGeom prst="rect">
						<a:avLst/>
					</a:prstGeom>
					<a:solidFill>
						<a:srgbClr val="EEECE1"/>
					</a:solidFill>
					<a:ln>
						<a:solidFill>
							<a:schemeClr val="tx1"/>
						</a:solidFill>
					</a:ln>
				</p:spPr>
				<p:txBody>
					<a:bodyPr wrap="none" rtlCol="0">
						<a:spAutoFit/>
					</a:bodyPr>
					<a:lstStyle/>
					<a:p>
						<a:r>
							<a:rPr lang="en-US" dirty="0" smtClean="0"/>
							<a:t>$slide.command$</a:t>
						</a:r>
						<a:endParaRPr lang="en-US" dirty="0"/>
					</a:p>
				</p:txBody>
			</p:sp>			
		</p:spTree>
		<p:extLst>
			<p:ext uri="{BB962C8B-B14F-4D97-AF65-F5344CB8AC3E}">
				<p14:creationId xmlns:p14="http://schemas.microsoft.com/office/powerpoint/2010/main" val="2830902126"/>
			</p:ext>
		</p:extLst>
	</p:cSld>
	<p:clrMapOvr>
		<a:masterClrMapping/>
	</p:clrMapOvr>
</p:sld>


>>
