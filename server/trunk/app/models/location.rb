class Location < ActiveRecord::Base
  has_one :time_entry
  has_one :customer

  validates :latitude, :longitude, :presence => true
end
