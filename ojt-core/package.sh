
VERSION=0.6-beta

mkdir -p libs/jexcelapi
cp /home/fabien/.m2/repository/net/sourceforge/jexcelapi/jxl/2.6/jxl-2.6.jar libs/jexcelapi/jxl.jar


zip -r ojt-$VERSION.zip \
	*.bat \
	*.sh \
	modele.xls \
	target \
	libs/jexcelapi/jxl.jar \
	changelog

