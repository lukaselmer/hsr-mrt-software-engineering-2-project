# Converts an Address to coordinates
class Geocoder
  def self.translate address_str
    return :error => "You must provide an address" if address_str.nil? || address_str.empty?
    request = "http://maps.google.com/maps/api/geocode/json?address=#{address_str}&sensor=true"
    resp = Net::HTTP.get_response(URI.parse(URI.escape(request)))
    return :error => "The request sent to google was invalid (not http success): #{request}. Response was: #{resp}" if !resp.is_a?(Net::HTTPSuccess)
    parsed_json = ActiveSupport::JSON.decode(resp.body)
    return :error => "The address you passed seems invalid, status was: #{parsed_json["status"]}.Request was: #{request}" if parsed_json["status"] != "OK"
    result = parsed_json["results"].first
    {:latitude => result["geometry"]["location"]["lat"],
      :longitude => result["geometry"]["location"]["lng"] } 
  end

  def self.address_to_gps_position address
    res = translate(address.to_google_maps_address)
    return nil unless res[:error].nil?
    GpsPosition.new(:latitude => res[:latitude], :longitude => res[:longitude])
  end
end
