////////////////////////////////////////////////////////////////////////////////
// $Id: P3SpellerTask.cpp 4008 2012-05-15 12:42:40Z mellinger $
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
//
//
// $Id: P3SpellerTask.cpp 2014-09-12 21:27
// Editor: Markus Strobel - mksstrobel@googlemail.com
// Description: This class is originally used for the BCI2000 P300 Speller and modified to provide a custom size matrix visualisation on a game background to get control input using the p300 paradigm.
//
////////////////////////////////////////////////////////////////////////////////

#include "PCHIncludes.h"
#pragma hdrstop

#include "P3SpellerTask.h"

//#include "PrecisionTime.h"

#include "SpellerCommand.h"
#include "TextStimulus.h"
#include "FlashStimulus.h"
#include "ImageStimulus.h"
#include "SoundStimulus.h"
#include "SpeechStimulus.h"
#include "AudioSpellerTarget.h"

#include "Localization.h"
#include "FileUtils.h"
#include <algorithm>
#include <iomanip>

using namespace std;
using namespace GUI;

RegisterFilter( P3SpellerTask, 3 );

// CTORs & DTORs
P3SpellerTask::P3SpellerTask()
	: mNumberOfSequences( 0 ),
	//mInterpretMode_( InterpretModes::None ),
	//mDisplayResults( false ),
	mTestMode( false ),
	//mCurMenu( 0 ),
	mNumMatrixRows( 0 ),
	mNumMatrixCols( 0 ),
	mSequenceCount( 0 ),
	mSequencePos( mSequence.begin() ),
	mAvoidStimulusRepetition( false ),
	//mSleepMode( 0 ),
	mPaused( false ),
	//mpStatusBar( NULL ),
	//mpTextWindow( NULL ),
	mSummaryFile( SummaryFileExtension().c_str() ),
	mRunCount( 0 ),
	mNumSelections( 0 ),
	mSleepDuration( 0 ),

	// new
	mBackgroundObject( NULL )
{
	BEGIN_PARAMETER_DEFINITIONS
		"Application:Sequencing int NumberOfSequences= 15 15 1 % // "
		"number of sequences in a set of intensifications",

		"Application:Speller%20Targets matrix TargetDefinitions= "
		"36 "
		"{Display Enter Display%20Size Icon%20File Sound} "
		"A A 1 % % "  "B B 1 % % "  "C C 1 % % "  "D D 1 % % "  "E E 1 % % "  "F F 1 % % "
		"G G 1 % % "  "H H 1 % % "  "I I 1 % % "  "J J 1 % % "  "K K 1 % % "  "L L 1 % % "
		"M M 1 % % "  "N N 1 % % "  "O O 1 % % "  "P P 1 % % "  "Q Q 1 % % "  "R R 1 % % "
		"S S 1 % % "  "T T 1 % % "  "U U 1 % % "  "V V 1 % % "  "W W 1 % % "  "X X 1 % % "
		"Y Y 1 % % "  "Z Z 1 % % "  "1 1 1 % % "  "2 2 1 % % "  "3 3 1 % % "  "4 4 1 % % "
		"5 5 1 % % "  "6 6 1 % % "  "7 7 1 % % "  "8 8 1 % % "  "9 9 1 % % "  "_ %20 1 % % "
		"% % % // speller target properties",
		"Application:Speller%20Targets intlist NumMatrixColumns= 1 "
		"6 6 1 % // display matrices' column number(s)",
		"Application:Speller%20Targets intlist NumMatrixRows= 1 "
		"6 6 0 % // display matrices' row number(s)",

		"Application:Audio%20Stimuli int AudioStimuliOn= 0 "
		"0 0 1 // Audio Stimuli Mode (0=no, 1=yes) (boolean)",
		"Application:Audio%20Stimuli matrix AudioStimuliRowsFiles= "
		"{ 1 2 3 4 5 6 } " // row labels
		"{ filename } " // filename
		"./voice/1.wav "
		"./voice/2.wav "
		"./voice/3.wav "
		"./voice/4.wav "
		"./voice/5.wav "
		"./voice/6.wav "
		" // audio stimuli rows files ",
		"Application:Audio%20Stimuli matrix AudioStimuliColsFiles= "
		"{ 1 2 3 4 5 6 } " // column labels
		"{ filename } " // filename
		"./voice/a.wav "
		"./voice/b.wav "
		"./voice/c.wav "
		"./voice/d.wav "
		"./voice/e.wav "
		"./voice/f.wav "
		" // audio stimuli column files ",

		"Application:Speller%20Targets floatlist TargetWidth= 1 5 0 0 100 // "
		"target width in percent of screen width",
		"Application:Speller%20Targets floatlist TargetHeight= 1 5 0 0 100 // "
		"target height in percent of screen height",
		"Application:Speller%20Targets floatlist TargetTextHeight= 1 10 0 0 100 // "
		"height of target labels in percent of screen height",
		"Application:Speller%20Targets stringlist BackgroundColor= 1 0x000000 "
		"0x505050 % % // target background color (color)",
		"Application:Speller%20Targets stringlist TextColor= 1 0x000000 "
		"0x505050 % % // text color (color)",
		"Application:Speller%20Targets stringlist TextColorIntensified= 1 0x0000FF "
		"0x505050 % % // intensified text color (color)",
		"Application:Speller%20Targets intlist IconHighlightMode= 1 1 "
		"1 0 4 // icon highlight method "
		"0: Show/Hide, "
		"1: Intensify, "
		"2: Grayscale, "
		"3: Invert, "
		"4: Dim "
		"   (enumeration)",
		"Application:Speller%20Targets floatlist IconHighlightFactor= 1 0.5 "
		"0.5 0 % // scale factor for highlighted icon pixel values",

		"Application:Speller int FirstActiveMenu= 1 "
		"1 1 % // Index of first active menu",
		"Application:Speller float StatusBarSize= 10 0 0 100 // "
		"size of status bar in percent of screen height",
		"Application:Speller float StatusBarTextHeight= 8 0 0 100 // "
		"size of status bar text in percent of screen height",
		"Application:Speller string TextToSpell= % % % % // "
		" character or string to spell in offline copy mode",
		"Application:Speller string TextResult= % % % % // "
		"user spelling result",
		"Application:Speller int TestMode= 0 0 0 1 // "
		"select targets by clicking on their associated stimuli (0=no, 1=yes) (boolean)",
		"Application:Speller string DestinationAddress= % % % % // "
		"network address for speller output in IP:port format",

		"Application:Text%20Window int TextWindowEnabled= 0 "
		"0 0 1 // Show Text Window (0=no, 1=yes) (boolean)",
		"Application:Text%20Window int TextWindowLeft= 640 0 0 % // "
		"Text Window X location",
		"Application:Text%20Window int TextWindowTop= 0 0 0 % // "
		"Text Window Y location",
		"Application:Text%20Window int TextWindowWidth= 512 512 0 % // "
		"Text Window Width",
		"Application:Text%20Window int TextWindowHeight= 512 512 0 % // "
		"Text Window Height",
		"Application:Text%20Window string TextWindowFontName= Courier % % % // "
		"Text Window Font Name",
		"Application:Text%20Window int TextWindowFontSize= 10 4 1 % // "
		"Text Window Font Size",
		"Application:Text%20Window string TextWindowFilePath= % % % % // "
		"Path for Saved Text File (directory)",
		END_PARAMETER_DEFINITIONS

		BEGIN_STATE_DEFINITIONS
		"SelectedTarget 16 0 0 0",
		"SelectedRow     8 0 0 0",
		"SelectedColumn  8 0 0 0",
		"SpellerMenu     8 0 0 0",
		END_STATE_DEFINITIONS

		LANGUAGES "German",
		BEGIN_LOCALIZED_STRINGS
		"TIME OUT !!!",
		"Zeit abgelaufen!",
		"Waiting to start ...",
		"Warte ...",
		// "Sleeping--Select SLEEP twice to resume",
		//          "Angehalten: Zweimal SLEEP fur Weiter",
		// "Select SLEEP once more to resume",
		//          "Angehalten: Noch einmal SLEEP fur Weiter",
		//"Paused--Select PAUSE again to resume",
		//         "Angehalten: Noch einmal PAUSE fur Weiter",
		END_LOCALIZED_STRINGS
}

P3SpellerTask::~P3SpellerTask()
{
	mStimuli.DeleteObjects();
	Speller::DeleteObjects();
	delete mBackgroundObject;
	mBackgroundObject = nullptr;
}

void P3SpellerTask::OnPreflight( const SignalProperties& /*Input*/ ) const
{
	Parameter( "DestinationAddress" );

	State( "Running" );
	State( "Recording" );

	// Parameters accessed for the summary file only
	OptionalParameter( "ID_Montage" );
	OptionalParameter( "ID_Amp" );
	OptionalParameter( "ID_System" );
	OptionalParameter( "OperatorVersion" );
	OptionalParameter( "EEGSourceVersion" );
	OptionalParameter( "SignalProcessingVersion" );
	OptionalParameter( "ApplicationVersion" );
	OptionalParameter( "Classifier" );
}

void P3SpellerTask::OnInitialize( const SignalProperties& /*Input*/ )
{
	mStimuli.DeleteObjects();
	Speller::DeleteObjects();
	Associations().clear();
	
	// some init values
	mNumMatrixRows = 12;
	mNumMatrixCols = 10;  
	// left, top, right, bottom  
	LoadSpellerMatrix(0.0f, 0.0f, 1.0f, 1.0f, mNumMatrixRows, mNumMatrixCols, Display(), mStimuli, Associations(), *this);

	InitSequence();

	mNumberOfSequences = Parameter( "NumberOfSequences" );
	//mDisplayResults = ( Parameter( "DisplayResults" ) != 0 );
	mTestMode = ( Parameter( "TestMode" ) != 0 );
}

void P3SpellerTask::OnStartRun()
{
	InitTCPIP();
	
	State( "Running" ) = 1;
	AppLog << "*** AGSP3Speller Ready ***\n\n";
	mSummaryFile << "*** AGSP3Speller Ready ***\n";

	AppLog << flush;


	// Non-summary file
	Display().ClearClicks();

	InitSequence();
	//DetermineAttendedTarget();
	//DisplayMessage( LocalizableString( "Waiting to start ..." ) );

	mNumSelections = 0;
	mSleepDuration = 0;
	//mSleepMode = dontSleep;
	mPaused = false;
	
	ShowCursor(false);

	/**

	// Summary file
	mSummaryFile << "System ID = "  << OptionalParameter( "ID_System", "N/A" )  << '\t'
		<< "Amp ID = "     << OptionalParameter( "ID_Amp", "N/A" )     << '\t'
		<< "Montage ID = " << OptionalParameter( "ID_Montage", "N/A" ) << '\n'
		<< "\nSW Versions:\n";

	if( Parameters->Exists( "OperatorVersion" ) )
		mSummaryFile << "Operator:\n\t"
		<< Parameter( "OperatorVersion" )( "Revision" )
		<< '\n';
	if( Parameters->Exists( "EEGSourceVersion" ) )
		mSummaryFile << "EEGSource:\n\t"
		<< Parameter( "EEGSourceVersion" )( "Revision" )
		<< '\n';
	if( Parameters->Exists( "SignalProcessingVersion" ) )
		mSummaryFile << "Signal Processing:\n\t"
		<< Parameter( "SignalProcessingVersion" )( "Revision" )
		<< '\n';
	if( Parameters->Exists( "ApplicationVersion" ) )
		mSummaryFile << "Application:\n\t"
		<< Parameter( "ApplicationVersion" )( "Revision" )
		<< '\n';

	mSummaryFile << "\n---------------------------------------------------"
		<< endl;

	if( Parameters->Exists( "Classifier" ) )
	{
		mSummaryFile << "Classifier Matrix:\n";
		ParamRef Classifier = Parameter( "Classifier" );
		for( int row = 0; row < Classifier->NumRows(); ++row )
		{
			for( int col = 0; col < Classifier->NumColumns(); ++col )
				mSummaryFile << Classifier( row, col ) << ' ';
			mSummaryFile << '\n';
		}
		mSummaryFile << flush;
	}

	++mRunCount;



	AppLog << "Start of run " << mRunCount << " in online (free) mode\n";
	mSummaryFile << "*** START OF RUN " << mRunCount << " IN ONLINE MODE ***\n";

				**/



	/**
	mSummaryFile << "Date = " << StringDate() << "\t\t"
		<< "Time = " << StringTime() << "\n"
		<< "Num of Sequences = " << mNumberOfSequences
		<< "\nMATRIX SIZE(s)\n";

		**/

	/**
	int numMenus = 1;
	for( int i = 0; i < numMenus; ++i )
	mSummaryFile << 10 << " x "
	<< 10 << '\n';
	**/

	mSelectionSummary.str() = "Selections in this run:\n";
}

void P3SpellerTask::OnStopRun()
{
	ShowCursor(true);
	DisposeTCPIP();
	//mBackgroundObject->ClearBackground();
	//DisplayMessage( LocalizableString( "TIME OUT !!!" ) );

	// App log
	AppLog << "*** AGSP3Speller Stopped ***\n";
	AppLog << flush;
	mSummaryFile << "*** AGSP3Speller Stopped ***\n\n";
	
	/**
	// Summary file
	mSummaryFile << "*** RUN SUMMARY ***\n"
		<< "System Pause Duration (in seconds): " << mSleepDuration << '\n'
		<< "Number of Selections = " << mNumSelections << '\n';
	**/

	mSummaryFile << mSelectionSummary.str() << endl;
	mSelectionSummary.clear();
	mSelectionSummary.str( "" );
}

void P3SpellerTask::OnPreSequence()
{
	// reset old sequence etc
	mStimuli.DeleteObjects();
	Speller::DeleteObjects();
	Associations().clear();
	
	// param
	char paramsBuffer[64];
	bool receivedParams = false;
	int resParam = 0;
	
	AppLog << "***** Wait for Sequence \n";
	mSummaryFile << "** OnPreSequence() - Wait for Sequence Data  ***\n";
	AppLog << flush;
	do
	{
		resParam = recv(connectedSocket, paramsBuffer, sizeof(paramsBuffer), 0);
		if(resParam == SOCKET_ERROR || resParam == 0)
		{
			Sleep(50);
			if(State("Running") == 1)
			{
				continue;
			}
			else
			{
				StopRun();
				exit(0);
				return;
			}
		}
		else
		{
			receivedParams = true;
			//paramsBuffer[resParam] = '\0'; // GING MIT, aber auch OHNE
		}
	}
	while(!receivedParams);

	// Parse MatrixData

	int matrixLeft;
	int matrixTop;
	int matrixRight;
	int matrixBottom;
	int rows;
	int columns;

	char* s;
	s = strtok (paramsBuffer ,";");

	int token = 0;
	while (s != NULL)
	{
		printf ("%s\n", s);

		switch(token)
		{
		case 0:
			matrixLeft = atoi(s);
			break;
		case 1:
			matrixTop = atoi(s);
			break;
		case 2:
			matrixRight = atoi(s);
			break;
		case 3:
			matrixBottom = atoi(s);
			break;
		case 4:
			rows = atoi(s);
			break;
		case 5:
			columns = atoi(s);
			break;
			/**
		case 6:
			width = atoi(s);
			break;
		case 7:
			height = atoi(s);
			break;
			**/
		}

		s = strtok(NULL, ";");
		token++;
	}

	// interpret rows or columns = 0 as terminate command
	if(columns == -1 || rows == -1)
	{
		AppLog << "State Running set to false! Number of Rows/Columns was equal to -1. \\This will be interpreted as Stop Command from AGS \n\n";
		AppLog << flush;
		
		mSummaryFile << "** Received Row/Column value -1 -> set State(\"Running\") = 0  ***\n\n";
		State("Running") = 0;
		return;
	}

	mSummaryFile << "*MatrixData Recevied (" << matrixLeft << ", " << matrixTop << ", " << matrixRight << ", " << matrixBottom << ", " << rows << ", " << columns << ") \n";
	AppLog << "* MatrixData Recevied (" << matrixLeft << ", " << matrixTop << ", " << matrixRight << ", " << matrixBottom << ", " << rows << ", " << columns << ") \n";
	AppLog << flush;


	// screen 
	BYTE *biData = nullptr;
	biData = new BYTE[256*256*256];		
	const int packetSize = 8192;
	char recvBuffer[packetSize];
	int finalres = 0;
	int res = 0;
	int i = 0;
	bool received = false;
	bool loop = true;

	do
	{
		//memset(recvBuffer, 0, sizeof(recvBuffer));
		res = recv(connectedSocket, recvBuffer, packetSize, 0);
		if(res == SOCKET_ERROR || res == 0)
		{
			if(received)
			{
				loop = false;
			}
			Sleep(50);
			if(State("Running") == 1)
				continue;
			else
				return;
		}
		else
		{
			received = true;
			memcpy(biData+i, recvBuffer, res);

			i += res;

			if(res > 0)
			{
				finalres = res;
			}
		}
	}
	while(loop); 

	// recreate BITMAP
	HDC hDC = GetDC(GetDesktopWindow());
	BITMAPINFO bi;
	int biSize = sizeof(BITMAPINFO);
	memcpy(&bi, biData, biSize);


	float xResolution = 1024;
	float yResolution = 768;

	// create compatible hbitmap and fill data with SetDIBits
	HBITMAP hbmp = CreateCompatibleBitmap(GetDC(GetDesktopWindow()), bi.bmiHeader.biWidth, bi.bmiHeader.biHeight);
	SetDIBits(hDC, hbmp, 0, bi.bmiHeader.biHeight, biData+biSize, &bi, DIB_RGB_COLORS);

	AppLog << "* ScreenData Received (" << xResolution << ", " << yResolution << ") \n";
	AppLog << flush;

	delete biData;
	biData = nullptr;

	// compute matrix dimensions
	float left = (float)matrixLeft / xResolution;
	float top = (float)matrixTop / yResolution;
	float right = (float)matrixRight / xResolution;
	float bottom= (float)matrixBottom / yResolution;

	// Set SpellerMatrix
	mNumMatrixRows = rows;
	mNumMatrixCols = columns;
	// left, top, right, bottom  
	LoadSpellerMatrix(left, top, right, bottom, mNumMatrixRows, mNumMatrixCols, Display(), mStimuli, Associations(), *this);

	AppLog << "* MatrixData Set\n";
	AppLog << flush;

	// Set Background
	if( mBackgroundObject == NULL)
	{
		mBackgroundObject = new BackgroundObject(Display(), xResolution, yResolution);
	}
	mBackgroundObject->ClearBackground(); // to avoid artifacts
	GUI::Rect backgroundRect = { 0, 0, 1.0, 1.0 };
	mBackgroundObject->SetObjectRect(backgroundRect);
	mBackgroundObject->SetBackground(hbmp);

	// to repaint the entire rect
	Display().Invalidate();

	AppLog << "* ScreenData Set\n";	
	mSummaryFile << "** OnPreSequence() - Received Data ***\n\n";
	AppLog << flush;

	InitSequence();

}

void P3SpellerTask::DoPreSequence( const GenericSignal&, bool& /*doProgress*/ )
{
	//ShowCursor(false);
}

void P3SpellerTask::OnSequenceBegin()
{
	State( "SelectedRow" ) = 0;
	State( "SelectedColumn" ) = 0;
	State( "SelectedTarget" ) = 0;
}

void P3SpellerTask::OnPostRun()
{
	State( "SelectedRow" ) = 0;
	State( "SelectedColumn" ) = 0;
	State( "SelectedTarget" ) = 0;
}

int P3SpellerTask::OnNextStimulusCode()
{
	// Return values of this function determine sequencing in the following way:
	//  A zero stimulus code ends the current sequence of stimuli.
	//  A null sequence (no nonzero stimulus codes between two zero codes) ends
	//  the run.
	int result = 0;
	if( !mSequence.empty() )
	{
		if( mSequencePos == mSequence.end() )
		{ // During a run, we always use the same sequence object and re-shuffle it.
			//
			// Sequences should fulfil the constraint that no stimulus
			// presented on the previous sequence's last stimulus presentation
			// may be presented on the next sequence's first stimulus presentation
			// (unless this is impossible by the way stimuli are grouped).

			int prevStimulusCode = *mSequence.rbegin();
			do
			{
				random_shuffle( mSequence.begin(), mSequence.end(), RandomNumberGenerator );
			} while( mAvoidStimulusRepetition && Associations().StimuliIntersect( *mSequence.begin(), prevStimulusCode ) );

			mSequencePos = mSequence.begin();
			if( ++mSequenceCount == mNumberOfSequences )
			{
				result = 0;
				mSequenceCount = 0;
			}
			else
			{
				result = *mSequencePos++;
			}
		}
		else
		{
			result = *mSequencePos++;
		}
	}
	return result;
}

void P3SpellerTask::DoPostSequence( const GenericSignal&, bool& /*doProgress*/ )
{
	//ShowCursor(true);
}

Target* P3SpellerTask::OnClassResult( const ClassResult& inResult )
{  
	// We override the standard ClassResult handler
	// - to additionally provide the SelectedTarget, SelectedRow, SelectedColumn
	//   states,
	// - to handle user clicks on visual stimuli,
	// - to log the classification result.

	// Clear the display's queue of clicked objects, and store a pointer to the
	// last clicked stimulus.
	Stimulus* pClickedStimulus = NULL;
	while( !Display().ObjectsClicked().empty() )
	{
		Stimulus* pStimulus = dynamic_cast<Stimulus*>( Display().ObjectsClicked().front() );
		if( pStimulus != NULL )
			pClickedStimulus = pStimulus;
		Display().ObjectsClicked().pop();
	}

	Target* pTarget = NULL;
	if( mTestMode && pClickedStimulus != NULL )
	{ // Fake an ideal ERP result, i.e. a binary response to the clicked stimulus.
		// This allows for testing result processing as well.
		ClassResult fakeResult;
		GenericSignal fakeSignal( 1, 1 );
		for( AssociationMap::const_iterator i = Associations().begin();
			i != Associations().end(); ++i )
		{
			fakeSignal( 0, 0 ) =  i->second.Contains( pClickedStimulus );
			fakeResult[ i->first ].push_back( fakeSignal );
		}
		pTarget = Associations().ClassifyTargets( fakeResult ).MostLikelyTarget();
	}
	else
		pTarget = Associations().ClassifyTargets( inResult ).MostLikelyTarget();

	// Compute the "Selected*" states from the result.
	// These are for documentation purposes only, and may lose their meaning
	// when targets are not grouped into rows and columns.
	int targetID = pTarget ? pTarget->Tag() : 0;
	State( "SelectedTarget" ) = targetID;
	State( "SelectedRow" )    = targetID ? ( targetID - 1 ) / mNumMatrixCols + 1 : 0;
	State( "SelectedColumn" ) = targetID ? ( targetID - 1 ) % mNumMatrixCols + 1 : 0;

	// Write classification signal details into the application log.
	size_t numAverages = 0;
	for( ClassResult::const_iterator i = inResult.begin(); i != inResult.end(); ++i )
		numAverages += i->second.size();

	AppLog << "* Total Intensifications: " << numAverages * mNumberOfSequences << "\n";

	// Report mean responses to log file but not to screen log.
	AppLog.File << "Mean responses for each stimulus:\n";
	for( ClassResult::const_iterator i = inResult.begin(); i != inResult.end(); ++i )
	{
		float mean = 0.0;
		for( size_t j = 0; j < i->second.size(); ++j )
			mean += i->second[ j ]( 0, 0 );
		mean /= i->second.size();
		AppLog.File << "Response for Stimulus Code " << i->first << ": "
			<< setprecision( 2 ) << fixed << mean
			<< "\n";
	}
	
	AppLog << "* SelectedRow: " << State("SelectedRow") << "\n";
	AppLog << "* SelectedColumn: " << State("SelectedColumn") << "\n";	
	AppLog << "* ResultTarget: " << targetID << "\n";
	mSummaryFile << "* SelectedRow: " << State("SelectedRow") << "\n";
	mSummaryFile << "* SelectedColumn: " << State("SelectedColumn") << "\n";
	mSummaryFile << "* ResultTarget: " << targetID << "\n";
	AppLog << flush;
	
	char resultParam[32];
	// creating the parameter string
	sprintf_s(resultParam, "%d\0", targetID);
	send(connectedSocket, resultParam, sizeof(resultParam), 0);
	
	return pTarget;
}

void P3SpellerTask::InitSequence()
{
	AssociationMap::iterator i = Associations().find( 0 );
	if( i != Associations().end() )
		Associations().erase( i );

	mSequence.clear();
	for( AssociationMap::const_iterator i = Associations().begin();
		i != Associations().end(); ++i )
		mSequence.push_back( i->first );

	// Check whether it is possible to construct stimulus code sequences that avoid
	// stimulus repetitions.
	// If any stimulus code intersects with all other stimulus codes, avoiding
	// repetitions is impossible.
	mAvoidStimulusRepetition = true;
	for( size_t i = 0; mAvoidStimulusRepetition && i < mSequence.size(); ++i )
	{
		bool fullRowIntersects = true;
		for( size_t j = 0; fullRowIntersects && j < mSequence.size(); ++j )
			fullRowIntersects &= Associations().StimuliIntersect( mSequence[ i ], mSequence[ j ] );
		mAvoidStimulusRepetition &= !fullRowIntersects;
	}
	random_shuffle( mSequence.begin(), mSequence.end(), RandomNumberGenerator );
	mSequencePos = mSequence.begin();
	mSequenceCount = 0;
}

void P3SpellerTask::ClearSequence()
{
	mSequence.clear();
	mSequencePos = mSequence.begin();
	mSequenceCount = 0;
}

// Setup the SpellerMatrix
void P3SpellerTask::LoadSpellerMatrix(float left, float top, float right, float bottom, int numMatrixRows, int numMatrixCols, GUI::GraphDisplay& ioDisplay, SetOfStimuli& ioStimuli, AssociationMap& ioAssociations, Speller& ioSpeller ) const
{
	// Clear previously existing stimuli, speller targets, and stimulus-target associations.
	ioStimuli.DeleteObjects();
	ioSpeller.DeleteObjects();
	ioAssociations.clear();

	GUI::Rect ioRect = 
	{
		left, top, right, bottom
	};

	// Compute the enclosing rectangle's dimensions.
	// The speller matrix will be centered vertically and horizontally in the
	// rectangle as specified when LoadMenu() is called.
	float inputRectWidth = ioRect.right - ioRect.left,
		inputRectHeight = ioRect.bottom - ioRect.top,
		cellHeight = inputRectHeight / numMatrixRows,
		cellWidth = inputRectWidth / numMatrixCols,
		outputRectHeight = numMatrixRows * cellHeight,
		outputRectWidth = numMatrixCols * cellWidth;

	ioRect.left += ( inputRectWidth - outputRectWidth ) / 2;
	ioRect.top += ( inputRectHeight - outputRectHeight ) / 2;
	ioRect.right = ioRect.left + outputRectWidth;
	ioRect.bottom = ioRect.top + outputRectHeight;

	// For each target definition, create stimuli and speller targets.
	int numElements = numMatrixCols * numMatrixRows;
	for( int i = 0; i < numElements; ++i )
	{
		int targetCol = i % numMatrixCols,
			targetRow = i / numMatrixCols;
		Association& rowSet = ioAssociations[ targetRow + 1 ],
			& colSet = ioAssociations[ numMatrixRows + targetCol + 1 ];

		// SPELLER TARGETS
		AudioSpellerTarget* pTarget = new AudioSpellerTarget( ioSpeller );	
		ostringstream temp;
		temp << (i + 1);
		pTarget->SetEntryText( temp.str() )
			.SetTag( i + 1 );

		//pTarget->SetSound( TargetDefinitions( i, SoundFile ) );	
		rowSet.Add( pTarget );
		colSet.Add( pTarget );


		// FLASH STIMULUS
		FlashStimulus* pFlashStimulus = new FlashStimulus( ioDisplay );
		GUI::Rect flashTargetRect =
		{
			ioRect.left + targetCol * cellWidth,  // targetRect Left
			ioRect.top  + targetRow * cellHeight, // targetRect Top
			ioRect.left + ( targetCol + 1 ) * cellWidth, // targetRect Right
			ioRect.top  + ( targetRow + 1 ) * cellHeight // targetRect Bottom
		};

		pFlashStimulus->SetColor( RGBColor::Blue );
		pFlashStimulus->SetObjectRect( flashTargetRect );
		pFlashStimulus->SetAlpha( 128 );
		pFlashStimulus->SetMeshDrawing(true);
		pFlashStimulus->SetPresentationMode( VisualStimulus::ShowHide );

		ioStimuli.Add( pFlashStimulus );
		rowSet.Add( pFlashStimulus );
		colSet.Add( pFlashStimulus );
	}

	/**
	// Create audio stimuli for rows and columns.
	if( Parameter( "AudioStimuliOn" ) != 0 )
	{
	ParamRef AudioStimuliRowsFiles = Parameter( "AudioStimuliRowsFiles" );
	int entry = 0;
	if( AudioStimuliRowsFiles->NumColumns() > inMenuIdx )
	entry = inMenuIdx;
	for( int i = 0; i < AudioStimuliRowsFiles->NumRows(); ++i )
	{
	int row = ::atoi( AudioStimuliRowsFiles->RowLabels()[ i ].c_str() );
	AudioStimulus* pStimulus = new AudioStimulus;
	pStimulus->SetSound( AudioStimuliRowsFiles( i, entry ) );
	if( !pStimulus->Error().empty() )
	bcierr << "AudioStimulusRowsFiles(" << i << ", " << entry << "): "
	<< pStimulus->Error()
	<< endl;
	ioStimuli.Add( pStimulus );
	ioAssociations[ row ].Add( pStimulus );
	}
	ParamRef AudioStimuliColsFiles = Parameter( "AudioStimuliColsFiles" );
	entry = 0;
	if( AudioStimuliColsFiles->NumColumns() > inMenuIdx )
	entry = inMenuIdx;
	for( int i = 0; i < AudioStimuliColsFiles->NumRows(); ++i )
	{
	int col = ::atoi( AudioStimuliColsFiles->RowLabels()[ i ].c_str() );
	AudioStimulus* pStimulus = new AudioStimulus;
	pStimulus->SetSound( AudioStimuliColsFiles( i, entry ) );
	if( !pStimulus->Error().empty() )
	bcierr << "AudioStimulusColsFiles(" << i << ", " << entry << "): "
	<< pStimulus->Error()
	<< endl;
	ioStimuli.Add( pStimulus );
	ioAssociations[ numMatrixRows + col ].Add( pStimulus );
	}
	}

	**/
}

// Speller events.
void P3SpellerTask::OnEnter( const std::string& inText )
{	

	/**
	AppLog << "Selected command: " << inText << endl;
	AppLog << flush;
	
	
	char resultParam[32];
	
	// creating the parameter string
		sprintf_s(resultParam, "%s\0", test);
	// sending the parameters
		send(connectedSocket, resultParam, sizeof(resultParam), 0);

		**/

	AppLog << "***** End Sequence\n\n";
	AppLog << flush;

	
	/**
	if( mConnection.is_open() )
	mConnection << "P3Speller_Output " << inText << endl;
	**/
}

int P3SpellerTask::InitTCPIP()
{
	// *** Init TCP/IP Server ***
	AppLog << "** Initialize TCP/IP Connection **\n";

	// Init WinSocket
	WSADATA wsa;
	long result = WSAStartup(MAKEWORD(2,0),&wsa);
	if(result != 0)
	{
		AppLog << "Error: could not start winsocket, error code: " << result << "\n";
	}
	else
	{
		AppLog << "WinSocket started\n";
	}
	AppLog << flush;

	// create acceptSocket
	P3SpellerTask::acceptSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if(acceptSocket == INVALID_SOCKET)
	{
		AppLog << "Error: could not create socket, error code: " << WSAGetLastError() << "\n";
	}
	else
	{
		AppLog << "Socket created\n";
	}	
	AppLog << flush;

	// bind Socket
	u_short Port = 45011; // TODO -> move to parameter file
	memset(&addr, 0, sizeof(SOCKADDR_IN));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(Port);
	//addr.sin_addr.s_addr = ADDR_ANY;

	result = bind(acceptSocket, (SOCKADDR*)&addr, sizeof(SOCKADDR_IN));
	if(result == SOCKET_ERROR)
	{
		AppLog << "Error: could not bind Socket to Port: " << Port << ", error code: " << WSAGetLastError() << "\n";
	}
	else
	{
		AppLog << "Socket succesfully bound to Port: " << Port << "\n";
	}

	// Listen for client connection
	AppLog << "Start listen for client...\n";
	AppLog << flush;

	result = listen(acceptSocket, 10);
	if(result == SOCKET_ERROR)
	{
		AppLog << "Error: could not start to listen for client, error code: " << WSAGetLastError() << "\n";
	}

	// accept Connection
	P3SpellerTask::connectedSocket = accept(acceptSocket,NULL,NULL);
	if(connectedSocket == INVALID_SOCKET)
	{
		AppLog << "Error: could not accept client connection, error code: " << WSAGetLastError() << "\n";
	}
	else
	{
		AppLog << "Client connection accepted\n";
		agsConnection = true;
	}
	AppLog << flush;

	
	DWORD sock_timeout = 50;
	// GANZ WICHTIG, sonst bleibt der socket beim receive hängen.
	setsockopt(connectedSocket, SOL_SOCKET, SO_RCVTIMEO, (char*)&sock_timeout, sizeof(sock_timeout));


	return result;
}

void P3SpellerTask::DisposeTCPIP()
{
	char resultParam[32];	
	// sending terminate command
	sprintf_s(resultParam, "%d\0", -1);
	send(connectedSocket, resultParam, sizeof(resultParam), 0);

	closesocket(acceptSocket);
	closesocket(connectedSocket);
	WSACleanup();
	agsConnection = false;
	AppLog << "TCP/IP Connection closed \n";
	AppLog << flush;
}


// LOGGING 
string P3SpellerTask::StringTime()
{
	string result;
	time_t now;
	const int bufLen = 20;
	char timeBuf[ bufLen + 1 ];
	::tzset();
	::time( &now );
	if( ::strftime( timeBuf, bufLen, "%H:%M", ::localtime( &now ) ) )
		result = timeBuf;
	return result;
}
// LOGGING
string P3SpellerTask::StringDate()
{
	string result;
	time_t now;
	const int bufLen = 20;
	char timeBuf[ bufLen + 1 ];
	::tzset();
	::time( &now );
	if( ::strftime( timeBuf, bufLen, "%b/%d/%y", ::localtime( &now ) ) )
		result = timeBuf;
	return result;
}
// LOGGING
string P3SpellerTask::TimeStamp()
{
	string result;
	time_t now;
	const int bufLen = 20;
	char timeBuf[ bufLen + 1 ];
	::tzset();
	::time( &now );
	if( ::strftime( timeBuf, bufLen, "%b%d%y_%H%M", ::localtime( &now ) ) )
		result = timeBuf;
	return result;
}
// LOGGING
string P3SpellerTask::SummaryFileExtension()
{
	return string( "_" ) + TimeStamp() + "_summary.txt";
}
// LOGGING
string P3SpellerTask::DirectoryFileName() const
{
	string result = Parameter( "TextWindowFilePath" );
	if( !result.empty() )
		result = FileUtils::AbsolutePath( result ) + "/LastFile.txt";
	return result;
}



void P3SpellerTask::errhandler(char *msg, HWND hwnd)
{
	AppLog << msg << "\n";
}

void P3SpellerTask::CreateBMPFile(HWND hwnd, LPTSTR pszFile, PBITMAPINFO pbi, HBITMAP hBMP, HDC hDC) 
 { 
     HANDLE hf;                 // file handle  
    BITMAPFILEHEADER hdr;       // bitmap file-header  
    PBITMAPINFOHEADER pbih;     // bitmap info-header  
    LPBYTE lpBits;              // memory pointer  
    DWORD dwTotal;              // total count of bytes  
    DWORD cb;                   // incremental count of bytes  
    BYTE *hp;                   // byte pointer  
    DWORD dwTmp; 



	


    pbih = (PBITMAPINFOHEADER) pbi; 
    lpBits = (LPBYTE) GlobalAlloc(GMEM_FIXED, pbih->biSizeImage);

    if (!lpBits) 
         errhandler("GlobalAlloc", hwnd); 




    // Retrieve the color table (RGBQUAD array) and the bits  
    // (array of palette indices) from the DIB.  
    if (!GetDIBits(hDC, hBMP, 0, (WORD) pbih->biHeight, lpBits, pbi, DIB_RGB_COLORS)) 
    {
        errhandler("GetDIBits", hwnd); 
    }



    // Create the .BMP file.  
    hf = CreateFile(pszFile, 
                   GENERIC_READ | GENERIC_WRITE, 
                   (DWORD) 0, 
                    NULL, 
                   CREATE_ALWAYS, 
                   FILE_ATTRIBUTE_NORMAL, 
                   (HANDLE) NULL); 
    if (hf == INVALID_HANDLE_VALUE) 
        errhandler("CreateFile", hwnd); 
    hdr.bfType = 0x4d42;        // 0x42 = "B" 0x4d = "M"  
    // Compute the size of the entire file.  
    hdr.bfSize = (DWORD) (sizeof(BITMAPFILEHEADER) + 
                 pbih->biSize + pbih->biClrUsed 
                 * sizeof(RGBQUAD) + pbih->biSizeImage); 
    hdr.bfReserved1 = 0; 
    hdr.bfReserved2 = 0; 

    // Compute the offset to the array of color indices.  
    hdr.bfOffBits = (DWORD) sizeof(BITMAPFILEHEADER) + 
                    pbih->biSize + pbih->biClrUsed 
                    * sizeof (RGBQUAD); 

    // Copy the BITMAPFILEHEADER into the .BMP file.  
    if (!WriteFile(hf, (LPVOID) &hdr, sizeof(BITMAPFILEHEADER), (LPDWORD) &dwTmp,  NULL)) 
    {
       errhandler("WriteFile", hwnd); 
    }



    // Copy the BITMAPINFOHEADER and RGBQUAD array into the file.  
    if (!WriteFile(hf, (LPVOID) pbih, sizeof(BITMAPINFOHEADER) + pbih->biClrUsed * sizeof (RGBQUAD), (LPDWORD) &dwTmp, ( NULL)))
        errhandler("WriteFile", hwnd); 
	
    // Copy the array of color indices into the .BMP file.  
    dwTotal = cb = pbih->biSizeImage; 
    hp = lpBits; 
    if (!WriteFile(hf, (LPSTR) hp, (int) cb, (LPDWORD) &dwTmp,NULL)) 
           errhandler("WriteFile", hwnd); 

    // Close the .BMP file.  
     if (!CloseHandle(hf)) 
           errhandler("CloseHandle", hwnd); 

    // Free memory.  
    GlobalFree((HGLOBAL)lpBits);
}

