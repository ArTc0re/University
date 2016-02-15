////////////////////////////////////////////////////////////////////////////////
// $Id: BackgroundImage.cpp 2014-09-16 19:56
// Author: Markus Strobel - mksstrobel@googlemail.com
// Description: This class derives from GraphObject and paints a bitmap image to the background at GraphObjects' SceneDisplayZOrder
//
////////////////////////////////////////////////////////////////////////////////


#include "BackgroundObject.h"

using namespace std;
using namespace GUI;

// CTORS & DTORS
BackgroundObject::BackgroundObject(GUI::GraphDisplay& display, int width, int height) 
	: GraphObject( display, SceneDisplayZOrder ),
	mPixmap (width, height)
{
}

BackgroundObject::~BackgroundObject(void)
{
}



// METHODS
void BackgroundObject::OnPaint(const GUI::DrawContext& drawContext)
{
	DoPaint(drawContext);
}

void BackgroundObject::DoPaint(const GUI::DrawContext& drawContext )
{
	QPainter* p = drawContext.handle.painter;	
	QRect rect(
    static_cast<int>( drawContext.rect.left ), // left
    static_cast<int>( drawContext.rect.top ), // top
    static_cast<int>( drawContext.rect.right - drawContext.rect.left ), // width
    static_cast<int>( drawContext.rect.bottom - drawContext.rect.top ) // height
	);

	p->drawPixmap(rect, mPixmap);

}

void BackgroundObject::ClearBackground()
{
	mPixmap.fill(Qt::black);
}

// Getter 

// Setter 
void BackgroundObject::SetBackground(HBITMAP hbmp)
{
	mPixmap = QPixmap::fromWinHBITMAP(hbmp, QPixmap::PremultipliedAlpha);
}

void BackgroundObject::SetBackground(QPixmap pixmap)
{
	mPixmap = pixmap;
}