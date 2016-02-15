////////////////////////////////////////////////////////////////////////////////
// $Id: BackgroundObject.h 2014-09-16 19:56
// Author: Markus Strobel - mksstrobel@googlemail.com
// Description: This class derives from GraphObject and paints a bitmap image to the background at GraphObjects' SceneDisplayZOrder
//
////////////////////////////////////////////////////////////////////////////////

#include "GraphObject.h"

class BackgroundObject : public GUI::GraphObject
{
public:
	// ctors & dtors
	BackgroundObject(GUI::GraphDisplay& display, int width, int height);
	~BackgroundObject(void);


	void ClearBackground();
  // Properties
  // GETTER


  // SETTER
	void SetBackground(QPixmap qpixmap);
	void SetBackground(HBITMAP hbmp);


protected:
  // GraphObject event handlers
  virtual void OnPaint( const GUI::DrawContext& );
  
  void DoPaint( const GUI::DrawContext&);


private:
		QPixmap mPixmap;
		int mWidth, mHeight;	



};

