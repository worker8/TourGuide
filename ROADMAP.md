# Feature:
## Overlay Feature:
- Overlay: Add reveal effect for the circle overlay, reference project: https://github.com/itzikBraun/TutorialView

## ToolTip
- ToolTip: Make ToolTip accept custom layout, for example:
  toolTip.setCustomToolTipLayout(R.layout.sample_layout), so user will have no
restriction on how the tooltip should look like
- ToolTip: add little arrow pointing to targetted button

## Pointer
- Pointer: animate shadow to give an illusion of elevation, when button is big (high elevation), shadow should be big and faded, when button is smaller (low elevation), shadow should be small and darken. Reference: http://i.imgur.com/pEevcJ5.png
- Pointer: Add option to let user input their own Pointer, for example:
  pointer.setCustomPointerLayout(R.layout.sample_pointer_layout)
- Pointer: Add more default Pointer animation to indicate actions like:
  double click, swiping, double finger tapping etc.

# Stability
- Add some tests

# Todo (chores):
- Compile example app as release build to be submitted to google playstore
- Promote snapshot into release build into maven

