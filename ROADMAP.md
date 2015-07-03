# Feature:
## TourGuide Class itself:
- Able to use one instance of TourGuide throughout a Tour. So that we can do: 
    mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(pointer)
                .setToolTip(toolTip)
                .setOverlay(null)
                .playOn(button);
   button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                mTutorialHandler.playOn(button2);
            }
        });
  This is currently buggy.

## Overlay Feature:
- Overlay: Add reveal effect for the circle overlay, reference project: https://github.com/itzikBraun/TutorialView
- Overlay: Add an optional/customizable dismiss button on an Overlay

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
  long press, double click, swiping, double finger tapping etc.

# Stability
- Add some tests

# Todo:
- Figure out minSDK that can work with TourGuide
- Compile example app as release build to be submitted to google playstore
- Promote snapshot into release build into maven
- Add example to show multiple ToolTip without Overlay
