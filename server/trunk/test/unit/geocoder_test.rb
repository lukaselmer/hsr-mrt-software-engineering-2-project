require 'test_helper'

class GeocoderTest < ActiveSupport::TestCase
  test "valid address translation" do
    res = Geocoder.translate("Hungerbergstrasse 16, 8046 Zuerich")
    assert_nil res[:error]
    assert_in_delta 47.4201566, res[:latitude], 0.00001
    assert_in_delta 8.4998559, res[:longitude], 0.00001
  end

  test "valid address translation with umlauts" do
    res = Geocoder.translate("Hungerbergstrasse 16, 8046 Zürich")
    assert_nil res[:error]
    assert_in_delta 47.4201566, res[:latitude], 0.00001
    assert_in_delta 8.4998559, res[:longitude], 0.00001
  end
  
  test "invalid address translation" do
    res = Geocoder.translate("bubububu")
    assert_not_nil res[:error]
  end

  test "address to gps position" do
    a = Address.create(:line1 => "Hungerbergstrasse 16", :zip => 8046, :place => "Zürich")
    gps_position = Geocoder.address_to_gps_position(a)
    assert_not_nil gps_position
    assert_in_delta 47.4201566, gps_position.latitude, 0.00001
    assert_in_delta 8.4998559, gps_position.longitude, 0.00001
    assert gps_position.new_record?
  end
end
