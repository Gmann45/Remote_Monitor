package com.finalproject.monitor;
import com.sun.jna.Memory;
import static jtermios.windows.WinAPI.*;
import jtermios.windows.WinAPI.*;

public class TestSuite {
	public static void main(String[] args) {
		String COM = "COM4:";
		HANDLE hComm = CreateFileA(COM, GENERIC_READ | GENERIC_WRITE, 0, null, OPEN_EXISTING, FILE_FLAG_OVERLAPPED, null);

		check(SetupComm(hComm, 2048, 2048), "SetupComm ");

		DCB dcb = new DCB();
		dcb.DCBlength = dcb.size();
		dcb.BaudRate = CBR_1200;
		dcb.ByteSize = 8;
		dcb.fFlags = 0;
		dcb.Parity = NOPARITY;
		dcb.XonChar = 0x11;
		dcb.StopBits = ONESTOPBIT;
		dcb.XonChar = 0x13;

		check(SetCommState(hComm, dcb), "SetCommState ");

		COMMTIMEOUTS touts = new COMMTIMEOUTS();
		check(SetCommTimeouts(hComm, touts), "SetCommTimeouts ");

		check(!INVALID_HANDLE_VALUE.equals(hComm), "CreateFile " + COM);
		String send = "Hello World";
		int tlen = send.getBytes().length;

		int[] txn = { 0 };
		Memory txm = new Memory(tlen + 1);
		txm.clear();
		txm.write(0, send.getBytes(), 0, tlen);

		int[] rxn = { 0 };
		Memory rxm = new Memory(tlen);

		OVERLAPPED osReader = new OVERLAPPED();
		osReader.writeField("hEvent", CreateEventA(null, true, false, null));
		check(osReader.hEvent != null, "CreateEvent/osReader");

		OVERLAPPED osWriter = new OVERLAPPED();
		osWriter.writeField("hEvent", CreateEventA(null, true, false, null));
		check(osWriter.hEvent != null, "CreateEvent/osWriter");

		boolean first = false;
		check(ResetEvent(osWriter.hEvent), "ResetEvent/osWriter.hEvent");
		boolean write = WriteFile(hComm, txm, tlen, txn, osWriter);
		if (!write) {
			check(GetLastError() == ERROR_IO_PENDING, "WriteFile");
			System.out.println("Write pending");
			}
		while (!write) {
			System.out.println("WaitForSingleObject/write");
			int dwRes = WaitForSingleObject(osWriter.hEvent, 1000);
			switch (dwRes) {
				case WAIT_OBJECT_0:
					if (!GetOverlappedResult(hComm, osWriter, txn, true))
						check(GetLastError() == ERROR_IO_INCOMPLETE, "GetOverlappedResult/osWriter");
					else
						write = true;
					break;
				case WAIT_TIMEOUT:
					System.out.println("write TIMEOT");
					break;
				default:
					check(false, "WaitForSingleObject/write");
					break;
				}
			}
		System.out.println("Transmit: '" + txm.getString(0) + "' , len=" + txn[0]);

		check(ResetEvent(osReader.hEvent), "ResetEvent/osReader.hEvent ");
		boolean read = ReadFile(hComm, rxm, tlen, rxn, osReader);
		if (!read) {
			check(GetLastError() == ERROR_IO_PENDING, "ReadFile");
			System.out.println("Read pending");
			}

		while (!read) {
			System.out.println("WaitForSingleObject/read");
			check(ResetEvent(osReader.hEvent), "ResetEvent/osReader.hEvent");
//			int dwRes = WaitForSingleObject(osReader.hEvent, 100);
			int dwRes = WaitForMultipleObjects(100, osReader.hEvent, true, 1000);
			wait
//			wait
			switch (dwRes) {
				case WAIT_OBJECT_0:
					if (!GetOverlappedResult(hComm, osReader, rxn, false))
						check(GetLastError() == ERROR_IO_INCOMPLETE, "GetOverlappedResult/osReader");
					else
						System.out.println("Received: '" + rxm.getString(0) + "' , len=" + rxn[0]);
						//read = true;
					break;
				case WAIT_TIMEOUT:
					System.out.println("WAIT_TIMEOUT");
					break;
				default:
					check(false, "WaitForSingleObject/osReader.hEvent");
					break;
				}
			}

		System.out.println("Received: '" + rxm.getString(0) + "' , len=" + rxn[0]);
		check(CloseHandle(osWriter.hEvent), "CloseHandle/osWriter.hEvent");
		check(CloseHandle(osReader.hEvent), "CloseHandle/osReader.hEvent");
		check(CloseHandle(hComm), "CloseHandle/hComm");
		}

	private static void check(boolean isOk, String what) {
		if (!isOk) {
			System.err.println(what + " failed, error " + GetLastError());
			System.exit(0);
			}
		}
	}