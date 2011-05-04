class GpsPosition < ActiveRecord::Base
  has_one :time_entry
  has_one :customer

  validates :latitude, :longitude, :presence => true

  def to_s
    "#{latitude} / #{longitude}"
  end
end
