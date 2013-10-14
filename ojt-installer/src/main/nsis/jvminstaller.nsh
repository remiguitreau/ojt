;Utilisation

; Déclaration manuelle;

;     !include jvminstaller.nsh    

;     Page custom ShowInstallServiceMainPage ValidateInstallServicePage

; Ou par la macro NC_INSTALL_SERVICE_PAGE

;     !include jvminstaller.nsh

;     !insertmacro NC_INSTALL_SERVICE_PAGE


!ifndef NC_JVM_INSTALLER_INCLUDED

	!define NC_JVM_INSTALLER_INCLUDED
	
	!include statectrl.nsh
	
	;- = section cachée
	Section -JVM
		!insertmacro InitReturnCode
		!insertMacro SetCurrentSection "JVM Installer"

		IfErrors 0 +2
			Abort "Error before starting section JVM"

		;Debug ultime : si java.exe existe, on ne dézippe pas las JVM!
        IfFileExists $INSTDIR\java\bin\java.exe end

		startjvminstall:
		SetOutPath "$INSTDIR\java"
		SetOverwrite on
		DetailPrint "Extracting JVM installer : $TEMP/jvminstaller.exe"
		File ..\..\..\target_install\jvminstaller.exe
		ReserveFile ..\..\..\target_install\jvminstaller.exe
		DetailPrint "Installing JVM in $INSTDIR\java"
		; on lance l'install en mode silencieux (/S), en forcant le path (/D)
		ExecWait '"$INSTDIR\java\jvminstaller.exe" /S /D=$INSTDIR\java' $R9
		${If} $R9 != 0
			MessageBox MB_ABORTRETRYIGNORE "Unable to install JVM (error code is  : $R9)" /SD IDABORT IDRETRY startjvminstall IDABORT abortinstall
		${EndIf}
		DetailPrint "JVM installed in $INSTDIR\java, return code : $R9"
		goto done
		abortinstall:
			abort "Unable to install JVM (error code is  : $R9)"
		done:
			Delete $INSTDIR\java\jvminstaller.exe
		end:

		IfErrors 0 +2
			Abort "Error at end of section JVM"

		SectionEnd

	Section "un.JVM"
		RmDir /r "$INSTDIR\java"
	SectionEnd
!endif
