import java.io.*;

class Firewall
{
	String filename;
	
	Firewall(String fn)
	{
		filename = fn;	
	}
	
	boolean accept_packet(String direction, String protocol, int port, String ip_address)
	{
		return (check_direction(direction) &&
			check_protocol(protocol) &&
			check_port(port) &&
			check_ip(ip_address));
	}
	
	boolean check_direction(String direction)
	{
		return (direction.equals("inbound") || 
			direction.equals("outbound"));
	}
	
	boolean check_protocol(String protocol)
	{
		return (protocol.equals("tcp") || 
			protocol.equals("udp"));
	}

	boolean check_port(int port)
	{
		// for a single port
		return (port >= 1 && port <= 65535);
	}
	
	boolean check_ip(String ip_address)
	{
		String[] parsed_ip = ip_address.split("-");
		
		// for an IP range
		if (parsed_ip.length == 2)
		{
			int equals_counter = 0;
			String[] ip1 = parsed_ip[0].split("\\.");
			String[] ip2 = parsed_ip[1].split("\\.");
			for (int i = 0; i < 4; ++i)
			{
				int octet1 = Integer.parseInt(ip1[i]);
				int octet2 = Integer.parseInt(ip2[i]);
				if (octet1 > octet2 ||
					octet1 < 0 || octet1 > 255 ||
					octet2 < 0 || octet2 > 255)
				{
					return false;
				}
				if (octet1 == octet2)
				{
					++equals_counter;
				}
			}
			if (equals_counter == 4)
			{
				return false;
			}
		}
	
		// for a single IP address
		else
		{
			String[] ip = parsed_ip[0].split("\\.");
			for (int i = 0; i < 4; ++i)
			{
				int octet = Integer.parseInt(ip[i]);
				if (octet < 0 || octet > 255)
				{
					return false;
				}
			}
				
		}
		return true;
	}
}

public class Main
{
	public static void main(String[] args) throws IOException
        {
		Firewall FW = new Firewall("/path/to/fw.csv");
		
                File file = new File(FW.filename);
                BufferedReader br = new BufferedReader(new FileReader(file));
		
		String s;
		String[] parsed;
		while((s = br.readLine()) != null)
		{
			parsed = s.split(",");
			if (FW.accept_packet(parsed[0],parsed[1],Integer.parseInt(parsed[2]), parsed[3]))
			{
				System.out.print("ACCEPTED PACKET: ");
				System.out.println(s);
			}
			else
			{
				System.out.print("BLOCKED PACKET: ");
				System.out.println(s);
			}
		}
		br.close();
			
	}	
}


