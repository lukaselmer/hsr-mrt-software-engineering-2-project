class Location < ActiveRecord::Base
  has_one :time_entry
  has_one :customer
end
