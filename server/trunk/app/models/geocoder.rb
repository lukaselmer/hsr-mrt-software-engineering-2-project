# Converts an address to coordinates
class Geocoder
  def self.translate address
    return :error => "You must provide an address" if address.nil? || address.empty?
    request = "http://maps.google.com/maps/api/geocode/json?address=#{address}&sensor=true"
    resp = Net::HTTP.get_response(URI.parse(URI.escape(request)))
    return :error => "The request sent to google was invalid (not http success): #{request}. Response was: #{resp}" if !resp.is_a?(Net::HTTPSuccess)
    parsed_json = ActiveSupport::JSON.decode(resp.body)
    return :error => "The address you passed seems invalid, status was: #{parsed_json["status"]}.Request was: #{request}" if parsed_json["status"] != "OK"
    result = parsed_json["results"].first
    {:latitude => result["geometry"]["location"]["lat"],
      :longitude => result["geometry"]["location"]["lng"] } 
  end
end
