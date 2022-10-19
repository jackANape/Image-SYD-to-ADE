
public class Records {

	private String ID;
	private String productDescription;
	private String innerBarcode;
	private String outerBarcode;
	private String shipperBarcode;
	
	
	public Records(String ID, String prodDesc, String innerBarcode, String outerBarcode, String shipperBarcode)
	{
		this.ID = ID;	
		this.productDescription = prodDesc;
		this.innerBarcode = innerBarcode;
		this.outerBarcode = outerBarcode;
		this.shipperBarcode = shipperBarcode;
	}
	
	public String getID()
	{
		return ID;
	}

	public String getProdDesc()
	{
		return productDescription;
	}
	
	public String getInnerBarcode()
	{
		return innerBarcode;
	}
	
	public String getOuterBarcode()
	{
		return outerBarcode;
	}
	
	public String getShipperBarcode()
	{
		return shipperBarcode;
	}
	
		
	
	
	
	
	
	
	
	
	
	
}
