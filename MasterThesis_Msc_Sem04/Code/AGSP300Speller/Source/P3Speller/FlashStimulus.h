////////////////////////////////////////////////////////////////////////////////
// $Id: FlashStimulus.h 2014-09-12 21:27
// Author: Markus Strobel - mksstrobel@googlemail.com
// Description: This class derives from VisualStimulus and GraphObject and paints a rect with specific size, color, transparency and position.
//
////////////////////////////////////////////////////////////////////////////////


#include "VisualStimulus.h"
#include "GraphObject.h"
#include "Color.h"


class FlashStimulus : public VisualStimulus, public GUI::GraphObject
{
public:
	// ctors & dtors
	FlashStimulus(GUI::GraphDisplay& display);
	~FlashStimulus(void);
	
  // Properties
  // GETTER
	RGBColor GetColor() const; // get the color of the stimulus rect
	int GetAlpha() const; // get the transparency of the stimulus rect

  // SETTER
	void SetColor( RGBColor );
	void SetAlpha( int alpha ); // a value between 0 and 255
	void SetMeshDrawing(bool paintMatrixMesh); // if true the matrix mesh will be drawn

 protected:
  // GraphObject event handlers
  //virtual void OnChange( GUI::DrawContext& );
  virtual void OnPaint( const GUI::DrawContext& );

  // We introduce a separate, protected DoPaint() function to simplify
  // implementation of a derived FlashStimulus class that renders itself
  void DoPaint( const GUI::DrawContext&, QColor color, float alpha );

  // MEMBER VARIABLES
private:
	int			mAlpha;
	RGBColor	mColor;
	bool		mDrawMesh;
};

