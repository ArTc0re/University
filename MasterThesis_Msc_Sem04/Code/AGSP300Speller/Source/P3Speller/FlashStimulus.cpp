////////////////////////////////////////////////////////////////////////////////
// $Id: FlashStimulus.cpp 2014-09-12 21:27
// Author: Markus Strobel - mksstrobel@googlemail.com
// Description: This class derives from VisualStimulus and GraphObject and paints a rect with specific size, color, transparency and position.
//
////////////////////////////////////////////////////////////////////////////////


#include "FlashStimulus.h"
#include "Color.h"


using namespace std;
using namespace GUI;

// CTORS & DTORS
FlashStimulus::FlashStimulus( GraphDisplay& display) : GraphObject( display, ImageStimulusZOrder )
{
	mDrawMesh = true;
}
FlashStimulus::~FlashStimulus()
{ 
}



// METHODS
void FlashStimulus::OnPaint(const GUI::DrawContext& drawContext)
{
	bool doPaint = false;
	QColor color = QColor(0,0,0,0);

	if( BeingPresented() ) // -> draw a rect, that a row or column of rects is presented
    switch( PresentationMode() )
    {
	  case Intensify:
      case Grayscale:
      case Invert:
      case Dim:
      case ShowHide:
        doPaint = true;
		color = QColor(GetColor().R(), GetColor().G(), GetColor().B(), GetAlpha());
        break;
    }
  else // not being presented -> draw nothing
    switch( PresentationMode() )
    {
      case Intensify:
      case Grayscale:
      case Invert:
      case Dim:
	  case ShowHide:
		  doPaint = mDrawMesh; // TODO Konfigurierbar, wenn true dann wird das Gitter gezeichnet, wenn false dann nicht
		  break;
    }	
	if( doPaint )
		FlashStimulus::DoPaint( drawContext, color, GetAlpha() );
}

void FlashStimulus::DoPaint(const GUI::DrawContext& drawContext, QColor color, float alpha)
{
	QPainter* p = drawContext.handle.painter;
	
	QRect rect(
    static_cast<int>( drawContext.rect.left ), // left
    static_cast<int>( drawContext.rect.top ), // top
    static_cast<int>( drawContext.rect.right - drawContext.rect.left ), // width
    static_cast<int>( drawContext.rect.bottom - drawContext.rect.top ) // height
	);

	p->fillRect(rect, QBrush(color));
	p->drawRect(rect);
}


// Getter
RGBColor FlashStimulus::GetColor() const
{
	return mColor;
}
int FlashStimulus::GetAlpha() const
{
	return mAlpha;
}

// Setter
void FlashStimulus::SetColor(RGBColor color)
{
	mColor = color;
}
void FlashStimulus::SetAlpha(int alpha)
{
	mAlpha = alpha;
}

void FlashStimulus::SetMeshDrawing(bool paintMatrixMesh)
{
	mDrawMesh = paintMatrixMesh;
}





