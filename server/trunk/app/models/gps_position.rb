# Holds information about a specific location such as an adress and is needed for distance calculations on the client
class GpsPosition < ActiveRecord::Base
  has_one :time_entry
  has_one :customer

  validates :latitude, :longitude, :presence => true

  def to_s
    "#{latitude}, #{longitude}"
  end
end
