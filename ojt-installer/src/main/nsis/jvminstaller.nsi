OutFile ../../../target_install/jvminstaller.exe
InstallDir c:\aa

;!define UMUI_SKIN "iv4alarms"
;!include "UMUI2.nsh"

;!insertmacro MUI_PAGE_LICENSE "License.rtf"
;!insertmacro MUI_PAGE_COMPONENTS
;Var STARTMENU_FOLDER
;!insertmacro MUI_PAGE_STARTMENU "Application" $STARTMENU_FOLDER

;SilentInstall silent

;RequestExecutionLevel user

;!define SF_SELECTED 1

;Choix du répertoire d'installation
;Page directory
;Page instfiles
;UninstPage uninstConfirm
;UninstPage instfiles

Function .onInit
         # `/SD IDYES' tells MessageBox to automatically choose IDYES if the installer is silent
  # in this case, the installer can only be silent if the user used the /S switch or if
  # you've uncommented line number 5
  MessageBox MB_YESNO|MB_ICONQUESTION "Would you like the installer to be silent from now on?" \
    /SD IDYES IDNO no IDYES yes

  # SetSilent can only be used in .onInit and doesn't work well along with `SetSilent silent'

  yes:
    SetSilent silent
    Goto done
  no:
    SetSilent normal
  done:
FunctionEnd

	Section  
; 	 IfSilent 0 +2
 ;            MessageBox MB_OK|MB_ICONINFORMATION 'This is a "silent" installer, installdir is >$INSTDIR<'
             
		SetOutPath "$INSTDIR"
		SetOverwrite on
		File /r /x src.zip /x sample /x demo "C:\Program Files\Java\jre6\*"
		DetailPrint "Installing JVM"
	SectionEnd

