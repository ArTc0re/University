////////////////////////////////////////////////////////////////////////////////
// $Id: P3SpellerTask.h 3901 2012-04-04 08:53:22Z mellinger $
// Authors: schalk@wadsworth.org, vkamat@cambridgeconsultants.com,
//   pbrunner@wadsworth.org, shzeng, juergen.mellinger@uni-tuebingen.de
// Description: The task filter for a P300 based speller providing multiple
//   menus.
//
// $BEGIN_BCI2000_LICENSE$
// 
// This file is part of BCI2000, a platform for real-time bio-signal research.
// [ Copyright (C) 2000-2012: BCI2000 team and many external contributors ]
// 
// BCI2000 is free software: you can redistribute it and/or modify it under the
// terms of the GNU General Public License as published by the Free Software
// Foundation, either version 3 of the License, or (at your option) any later
// version.
// 
// BCI2000 is distributed in the hope that it will be useful, but
//                         WITHOUT ANY WARRANTY
// - without even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License along with
// this program.  If not, see <http://www.gnu.org/licenses/>.
// 
// $END_BCI2000_LICENSE$
////////////////////////////////////////////////////////////////////////////////
#ifndef P3_SPELLER_TASK_H
#define P3_SPELLER_TASK_H

#include "StimulusTask.h"	// REQUIRED!!
#include "Speller.h"		// REQUIRED!!
#include "StatusBar.h"					// NOT REQUIRED
#include "TextWindow.h"					// NOT REQUIRED
#include "SockStream.h"

#include "BitmapImage.h"	// MAYBE REQUIRED OR MAYBE NOT
#include "BackgroundObject.h" // REQUIRED!! 

#include <stack>
#include <vector>
#include <ctime>

class P3SpellerTask : public StimulusTask, public Speller
{
public:
	P3SpellerTask();
	~P3SpellerTask();

protected:
	// StimulusTask events
	virtual void    OnPreflight( const SignalProperties& Input ) const;
	virtual void    OnInitialize( const SignalProperties& Input );
	virtual void    OnStartRun();
	virtual void    OnStopRun();
	virtual void    OnPreSequence();
	virtual void    DoPreSequence( const GenericSignal&, bool& doProgress );
	virtual void    OnSequenceBegin();
	virtual void    OnPostRun();
	virtual void    DoPostSequence( const GenericSignal&, bool& doProgress );
	virtual Target* OnClassResult( const ClassResult& );
	virtual int     OnNextStimulusCode();

	// Speller events
	virtual void    OnEnter( const std::string& );

private:
	//void CheckSwitchMenu();
	//void DetermineAttendedTarget();
	void InitSequence();
	void ClearSequence();

	// The LoadSpellerMatrix function fills its output containers with stimuli/graph objects.
	// and sets the dimension and size of the matrixRect
	void LoadSpellerMatrix(
		float left, 
		float top, 
		float right, 
		float bottom,
		int numMatrixRows, 
		int numMatrixCols, 
		GUI::GraphDisplay& ioDisplay, 
		SetOfStimuli& ioStimuli, 
		AssociationMap& ioAssociations, 
		Speller& ioSpeller 
		) const;

	// each boundary represents a float which is the % value of the screen
	void SetMatrixRectPosition(float left, float top, float right, float bottom);
	
	// For TCPIP Communication with agsP300Speller.dll
	int InitTCPIP();
	void DisposeTCPIP();
	void errhandler(char *msg, HWND hwnd);
	void CreateBMPFile(HWND hwnd, LPTSTR pszFile, PBITMAPINFO pbi, HBITMAP hBMP, HDC hDC);


	// Log file, text window file.
	static std::string TimeStamp();
	static std::string StringTime();
	static std::string StringDate();
	static std::string SummaryFileExtension();
	std::string DirectoryFileName() const;

	
	// MEMBER VARIABLES
private:
	// Configuration parameters.
	int  mNumberOfSequences;
	//bool mDisplayResults;
	bool	mTestMode;

	// For TCPIP Communication with agsP300Speller.dll
	SOCKET acceptSocket;
	SOCKET connectedSocket;
	SOCKADDR_IN addr;
	char spellerParams[128];
	char spellerResult[16];
	bool agsConnection;


	// Properties of the current menu.
	int mNumMatrixRows,
		mNumMatrixCols;

	// Internal state.
	int mSequenceCount;
	std::vector<int> mSequence;
	std::vector<int>::const_iterator mSequencePos;
	bool mAvoidStimulusRepetition;

	/**
	enum
	{
		dontSleep = 0,
		sleep1 = 1,
		sleep2 = 2,

		unsleepAfter

	};
	**/
	//int mSleepMode;
	bool mPaused;

	
	// Set of all existing stimuli.
	SetOfStimuli       mStimuli;

	// Display elements.
	BackgroundObject*  mBackgroundObject;

private:
	// Summary log file information.
	LogFile            mSummaryFile;
	std::ostringstream mSelectionSummary;
	int                mRunCount,
		mNumSelections,
		mSleepDuration;
	std::time_t        mStartPause;
};

#endif // P3_SPELLER_TASK_H
