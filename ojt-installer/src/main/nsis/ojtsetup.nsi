;--------------------------------
;Include Modern UI
!include "MUI2.nsh"

;--------------------------------
 # Macro declaration
!include "LogicLib.nsh"
!include "Ports.nsh"
!include "Host.nsh"
!include "UserMgr.nsh"
!include "AsciiFile.nsh"
!include "WinMessages.nsh"
!include "InstallOptions.nsh"
!include "IE.nsh"
!include "Utils.nsh"
;--------------------------------
; Mes Includes!
;--------------------------------

!define REGKEY "SOFTWARE\OJT"

!include statectrl.nsh
!include jvminstaller.nsh

;--------------------------------

# Defines
!define COMPANY OJT
!define URL "http://ojt.sourceforge.net"
!define PRODUCT_NAME "Open Judo Tournament"

!define OJT_REGKEY "SOFTWARE\OJT"
!define STARTMENU "$SMPROGRAMS\Open Judo Tournament"
!define prodname "OJT"
!define exec "bin\OJT.exe"

;VERSION est normalement définie par Maven !
!ifndef VERSION
        !define VERSION 1.1.2
!endif
!define VERSION_WITH_DOTS_ONLY 1.1.2.0

;--------------------------------
;General
; apparait en bas à gauche de la fenêtre d'installation
BrandingText "Open Judo Tournament (2011) - v. ${VERSION}"
;Name and file
Name "Open Judo Tournament"
Caption "Open Judo Tournament"

OutFile "..\..\..\target_install\OJT-install-${VERSION}.exe"

;Default installation folder
InstallDir "c:\OJT"

;Get installation folder from registry if available
InstallDirRegKey HKCU "$REGKEY" ""

;Request application privileges for Windows Vista : admin car installation d'un service
RequestExecutionLevel admin


CRCCheck on

XPStyle on
ShowInstDetails hide
ShowUninstDetails hide


SetDateSave on


;--------------------------------
;Interface Configuration : MUI2

;!define MUI_PAGE_HEADER_TEXT "MUI_PAGE_HEADER_TEXT"
;!define MUI_PAGE_HEADER_SUBTEXT "MUI_PAGE_HEADER_SUBTEXT"

; doit cliquer sur une checkbox pour accepter la licence
!define MUI_LICENSEPAGE_CHECKBOX

!define MUI_WELCOMEPAGE_TITLE "Installation de Open Judo Tournament"
;Extra space for the title area.
!define MUI_WELCOMEPAGE_TITLE_3LINES
;!define MUI_WELCOMEPAGE_TEXT "MUI_WELCOMEPAGE_TEXT"

!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_TEXT "Lancer Open Judo Tournament"
!define MUI_FINISHPAGE_RUN_FUNCTION "LaunchLink"

; 164x314
!define MUI_UNWELCOMEPAGE_TITLE_3LINES
!define MUI_UNFINISHPAGE_TITLE_3LINES

; Background color for the header, the Welcome page and the Finish page.
!define MUI_BGCOLOR C5CFD8
;96AFCF
; Directory page : The background color for the directory textbox.
;!define MUI_DIRECTORYPAGE_BGCOLOR 96AFCF
; License page :Couleur de fond de la page Licence
;!define MUI_LICENSEPAGE_BGCOLOR 96AFCF
; The background color for the startmenu directory list and textbox.
;!define MUI_STARTMENUPAGE_BGCOLOR 96AFCF
; The colors of the details screen. Use /windows for the default Windows colors. "foreground background"
;!define MUI_INSTFILESPAGE_COLORS "00FF00 0F0F0F"
;!define MUI_INSTALLCOLORS "3399CC" "FFFFFF"
;!define MUI_INSTFILESPAGE_PROGRESSBAR "colored"

!define MUI_HEADERIMAGE
; 150x57

!define MUI_LANGDLL_ALWAYSSHOW
!define MUI_LANGDLL_REGISTRY_ROOT HKLM
!define MUI_LANGDLL_REGISTRY_KEY "${REGKEY}"
!define MUI_LANGDLL_REGISTRY_VALUENAME "LANGUAGE"

;  !define MUI_ABORTWARNING


;!define MUI_FINISHPAGE_RUN "$INSTDIR\bin\OJT.exe"
;!define MUI_FINISHPAGE_RUN_TEXT "$(^SHORTCUT_HOME)"

;--------------------------------
;Pages
; Avant tout, on vérifie que l'on pourra installer l'application
;!define MUI_PAGE_CUSTOMFUNCTION_PRE preInstallCheck


!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "license.txt"
;Page custom showInstallCodePage checkInstallCode
;!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
;      Page license
;      Page directory
;     Page components
;!insertmacro NC_INSTALL_SERVICE_PAGE
;    Page instfiles

;   UninstPage uninstConfirm
;  UninstPage instfiles


;    !insertmacro MUI_PAGE_WELCOME
;    !insertmacro MUI_PAGE_LICENSE "${NSISDIR}\Docs\Modern UI\License.txt"
;    !insertmacro MUI_PAGE_COMPONENTS
;   !insertmacro MUI_PAGE_DIRECTORY
;  !insertmacro MUI_PAGE_INSTFILES
; !insertmacro MUI_PAGE_FINISH

!define MUI_WELCOMEPAGE_TITLE_3LINES
!define MUI_FINISHPAGE_TITLE_3LINES

!insertmacro MUI_UNPAGE_WELCOME
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH

!define MUI_CUSTOMFUNCTION_GUIINIT preInstallCheck


;--------------------------------
;Languages
;first language is the default language

!insertmacro MUI_LANGUAGE "French"

!include "i18n\fr.nsh"
!insertmacro utils_i18n
;--------------------------------
;Reserve Files

;If you are using solid compression, files that are required before
;the actual installation should be stored first in the data block,
;because this will make your installer start faster.

!insertmacro MUI_RESERVEFILE_LANGDLL
;!insertmacro installcode_reserve

;--------------------------------
; Infos sur l'exe d'installation
VIAddVersionKey /LANG=${LANG_FRENCH} "ProductName" "Open Judo Tournament"
VIAddVersionKey /LANG=${LANG_FRENCH} "Comments" "Open Judo Tournament"
VIAddVersionKey /LANG=${LANG_FRENCH} "CompanyName" "${COMPANY}"
VIAddVersionKey /LANG=${LANG_FRENCH} "LegalTrademarks" "OJT est sous LGPL"
VIAddVersionKey /LANG=${LANG_FRENCH} "LegalCopyright" "© ${COMPANY}"
VIAddVersionKey /LANG=${LANG_FRENCH} "FileDescription" "Open Judo Tournament"
VIAddVersionKey /LANG=${LANG_FRENCH} "FileVersion" "${VERSION}"
VIProductVersion "${VERSION_WITH_DOTS_ONLY}"

;--------------------------------
Function .onInit
	!insertmacro MUI_LANGDLL_DISPLAY
	${If} $LANGUAGE = "cancel"
	    Abort "Language selection cancelled. Aborting install"
	${EndIf}

 	  ; Extract InstallOptions files
 	  ; $PLUGINSDIR will automatically be removed when the installer closes
 	  InitPluginsDir
; 	  File /oname=$PLUGINSDIR\installcode.ini "installcode.ini"
	;!insertmacro installcode_oninit

FunctionEnd

;Installer Functions
; Function vérifiant que les conditions requises pour installer l'application sont OK.
Function preInstallCheck
	; On force la désinstallation de la précédente version
	ClearErrors

	DetailPrint "$(^CHECKING_INSTALL_PREREQUISITE)"

	IfErrors 0 +4
		Abort "Error after install check"
		Quit
		
	!insertmacro InitReturnCode
FunctionEnd
;--------------------------------
;Sections
Section "!OJT" MainSection
	!insertmacro InitReturnCode
	!insertMacro SetCurrentSection "Open Judo Tournament"

        DetailPrint "this message will show on the installation window"

	SetOutPath "$INSTDIR"

	File /r ..\..\..\target\ojt-installer-${VERSION}-bin-release-windows\*
	Delete "$INSTDIR\datas\export\readme.txt"
	Delete "$INSTDIR\datas\persistancy\readme.txt"
	Delete "$INSTDIR\datas\pesees\readme.txt"
	Delete "$INSTDIR\datas\source\readme.txt"	

	IfErrors 0 +2
		Abort "Error before starting section Main"

	;ADD YOUR OWN FILES HERE...
	SetOverwrite on
	DetailPrint "$(^INSTALLING_FILES)"
	SetOutPath "$INSTDIR\bin"
	File /r ..\..\..\target\ojt-installer-${VERSION}-bin-release-windows\bin\*
	IfErrors 0 +2
		Abort "Unable to install 'bin' folder"

	DetailPrint "this message will show on the installation window 2"
	
	DetailPrint "Installing OJT libs"
	SetOutPath "$INSTDIR\libs"
	File /r ..\..\..\target\ojt-installer-${VERSION}-bin-release-windows\libs\*
	
	;Store installation folder
	DetailPrint "Storing install informations..."
	WriteRegStr HKCU "${REGKEY}" "" $INSTDIR

	;Create uninstaller
	DetailPrint "$(^GENERATING_UNINSTALL_INFOS)"
	WriteUninstaller "$INSTDIR\Uninstall.exe"
	; Sous Vista, il faut avoir les droits d'administration
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\OJT" \
	"DisplayName" "Open Judo Tournament"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\OJT" \
	"Publisher" "OJT team"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\OJT" \
	"Version" "555"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\OJT" \
	"URLInfoAbout" "http://ojt.sourceforge.net/"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\OJT" \
	"DisplayIcon" "$INSTDIR\Uninstall.exe,0"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\OJT" \
	"UninstallString" "$INSTDIR\Uninstall.exe"

SectionEnd

;Section de vérification que l'installation s'est correctement déroulée
Section "!Check" Check
		DetailPrint "$(^CHECKING_INSTALLATION)"

        ${If} ${Errors}
       		Abort "Some errors occured"
        ${EndIf}
        
        DetailPrint "$(^CHECKING_INSTALLATION_SUCCESS)"

SectionEnd

; see http://nsis.sourceforge.net/CreateInternetShortcut_macro_&_function
; Usage :
; !insertmacro CreateInternetShortcut ...
  
!macro CreateInternetShortcut FILENAME URL ICONFILE ICONINDEX
WriteINIStr "${FILENAME}.url" "InternetShortcut" "URL" "${URL}"
!macroend


;Création des raccourcis dans le menu démarrer
Section
	DetailPrint "$(^CREATING_SHORTCUTS)"
	CreateDirectory "${startmenu}"
	
	SetOutPath "$INSTDIR\bin" ; for working directory	
	CreateShortCut "${startmenu}\Open Judo Tournament.lnk" "$INSTDIR\${exec}"	
	
	SetOutPath "INSTDIR"
	CreateShortCut "${startmenu}\$(^SHORTCUT_UNINSTALL).lnk" "$INSTDIR\uninstall.exe"

SectionEnd

# Fonction pour ouvrir un lien internet dans une nouvelle fenêtre
# Uses $0
Function myOpenLinkNewWindow
  Push $3
  Push $2
  Push $1
  Push $0
  ReadRegStr $0 HKCR "http\shell\open\command" ""
# Get browser path
    DetailPrint $0
  StrCpy $2 '"'
  StrCpy $1 $0 1
  StrCmp $1 $2 +2 # if path is not enclosed in " look for space as final char
    StrCpy $2 ' '
  StrCpy $3 1
  loop:
    StrCpy $1 $0 1 $3
    DetailPrint $1
    StrCmp $1 $2 found
    StrCmp $1 "" found
    IntOp $3 $3 + 1
    Goto loop

  found:
    StrCpy $1 $0 $3
    StrCmp $2 " " +2
      StrCpy $1 '$1"'

  Pop $0
  Exec '$1 $0'
  Pop $1
  Pop $2
  Pop $3
FunctionEnd

Function LaunchLink
	ExecShell "" "${startmenu}\Open Judo Tournament.lnk"
FunctionEnd

;--------------------------------
;Descriptions
	;Description localisées des sections
	!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
	!insertmacro MUI_DESCRIPTION_TEXT ${MainSection} $(DESC_MainSection)
	!insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "Uninstall"
	DetailPrint "Uninstallation started "
	
	; Suppr du service Windows (si il est enregistré ou démarré)
	;ADD YOUR OWN FILES HERE...
	DetailPrint "$(^UNINSTALLING_FILES)"
	Delete "$INSTDIR\Uninstall.exe"
	
	RmDir /r "$INSTDIR\bin"
	RmDir /r "$INSTDIR\libs"
	RmDir /r "$INSTDIR\LICENSES"
	
	; On supprime la JVM
	RmDir /r "$INSTDIR\java"
	
	RmDir /r "$INSTDIR\"

	DetailPrint "$(^UNINSTALLING_SHORTCUTS)"

	Delete "${startmenu}\*.*"
	Delete "${startmenu}"
	RmDir "$SMPROGRAMS\Open Judo Tournament"

	StrCpy $0 "$SMPROGRAMS\Open Judo Tournament"
	Call un.DeleteDirIfEmpty

	; Suppr du répertoire d'installation SSI il est vide
	StrCpy $0 "$INSTDIR"
	Call un.DeleteDirIfEmpty

	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\OJT"

	DeleteRegKey /ifempty HKCU ${REGKEY}
	DeleteRegKey /ifempty HKCU ${OJT_REGKEY}
SectionEnd

; http://nsis.sourceforge.net/Delete_dir_only_if_empty
; Exemple :
;   StrCpy $0 "$SMPROGRAMS\PwStore"
;   Call un.DeleteDirIfEmpty
Function un.DeleteDirIfEmpty
        FindFirst $R0 $R1 "$0\*.*"
        strcmp $R1 "." 0 NoDelete
                FindNext $R0 $R1
                strcmp $R1 ".." 0 NoDelete
                        ClearErrors
                        FindNext $R0 $R1
                        IfErrors 0 NoDelete
                                FindClose $R0
                                Sleep 1000
                                RMDir "$0"
        NoDelete:
                FindClose $R0
FunctionEnd

;--------------------------------
;Uninstaller Functions

Function un.onInit
	!insertmacro MUI_UNGETLANGUAGE
FunctionEnd
