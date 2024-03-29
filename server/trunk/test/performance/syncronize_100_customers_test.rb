require 'test_helper'
require 'rails/performance_test_help'

class Synchronize10CustomersTest < ActionDispatch::PerformanceTest
  def setup
    sign_in
    address = Address.create(:line1 => "Oberseestrasse 10", :zip => "8640", :place => "Rapperswil")
    
    100.times do
      Customer.create(:first_name => "Thomas", :last_name => "Tester", :phone => "0441231212", :address => address)
    end
  end

  def sign_in
    post_via_redirect "/users/sign_in", "user[email]" => "field_worker@mrt.ch", "user[password]" => "mrt"
    assert_equal "/", path
  end

  def test_synchronize_10_customers
    post '/customers/synchronize.json', :format => :json
  end
end
