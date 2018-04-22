import java.io.*;

class ReadFileToByte
{

	String _file;
	byte[] _bytetab;


	/**
	 *Konstruktor klasy
	 *@param f jest nazwą lub ścieżką do pliku który należy odczytać
	 */
	public ReadFileToByte(String f)
	{
		_file = f;
		
	}

	/**
	 *Metoda do wczytywania pliku do zmiennej typu byte[]
	 */
	public void readFile()
	{
		File file = new File(_file);
		FileInputStream fileinput = null;
		_bytetab = new byte[(int)file.length()];

		try
		{
			fileinput = new FileInputStream(file);
			fileinput.read(_bytetab);
			fileinput.close();
			
		}
		catch(IOException e)
		{
			System.out.println("ERROR");
		}

	}
	
	/**
	 *Zwraca tablice byte[]
	 */
	public byte[] getByteTab()
	{
		return _bytetab;
	}

}
