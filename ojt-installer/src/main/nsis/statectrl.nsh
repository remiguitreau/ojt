!ifndef NC_STATE_CTRL_INCLUDED

	!define NC_STATE_CTRL_INCLUDED

	Var state
	Var currentSection

	!macro InitReturnCode
	    StrCpy $state 0
		StrCpy $currentSection "not defined"
		DetailPrint "StateCtrl initialized : $currentSection / $state"
	!macroend

	!macro SetCurrentSection name
		   StrCpy $currentSection "${name}"
		   DetailPrint "In '$currentSection' Section"
	!macroend

	;le code de retour est stock� dans la var $0, on le sauvegarde dans $state
	!macro StoreReturnCode
		   StrCmp $0 "0" +2 0
		   StrCpy $state $0
	!macroend

	!macro CheckReturnCode
		   StrCmp $state "0" +5 0
		   DetailPrint "An error occured in section '$currentSection', err code is : '$state' - ABORT INSTALL!"
		   MessageBox MB_OK "$(^ErrorMessage) $currentSection "
		   Abort "$(^ErrorMessage) $currentSection "
		   SetErrorLevel $state
	!macroend

	!macro CheckReturnCodeWithMsg msg
		StrCmp $state "0" +5 0
		DetailPrint "ErrorMessage : ${msg}"
		MessageBox MB_OK "$(^ErrorMessage) $currentSection$\nMessage :    ${msg}"
		Abort "$(^ErrorMessage) $currentSection"
		SetErrorLevel $state
	!macroend


!endif
