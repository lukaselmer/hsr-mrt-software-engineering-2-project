# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ :name => 'Chicago' }, { :name => 'Copenhagen' }])
#   Mayor.create(:name => 'Daley', :city => cities[0])

### Clear all
[User, Material, TimeEntryType, TimeEntry, Address, Customer, Order, TimeEntry].each do |cls|
  cls.delete_all
end

### Users
Secretary.create!(:first_name => "Sec", :last_name => "Retary", :email => 'secretary@mrt.ch', :password => 'mrt')
FieldWorker.create!([
    { :first_name => "Fredi", :last_name => "Worker", :email => 'field_worker@mrt.ch', :password => 'mrt'},
    { :first_name => "Benny", :last_name => "Büezer", :email => 'bbuezer@mrt.ch', :password => 'mrt'},
  ])

### Materials
Material.create!([
    {:catalog_number => "3951.100", :description => "Füllventil Universal zu Spülkasten Geberit", :dimensions => "", :price => 34.0},
    {:catalog_number => "865005-336", :description => "Spültisch-Garnitur GEBERIT 1-teilig", :dimensions => "1 1/2 x 56mm", :price => 30.0},
    {:catalog_number => "046.101.000", :description => "Standrohrventil zu Spültisch Franke, ohne Sieb", :dimensions => "1 1/2", :price => 21.5},
    {:catalog_number => "863019-000", :description => "Bogen 90° GEBERIT mit Verschraubung", :dimensions => "56mm", :price => 15.0},
    {:catalog_number => "086105-000", :description => "Überwurfmutter GEBERIT Schleifing, Dichtung", :dimensions => "56mm", :price => 6.3},
    {:catalog_number => "51.000064", :description => "Verteilerkasten Grösse 3", :dimensions => "800mm", :price => 326.0},
    {:catalog_number => "51.043704", :description => "Anschlussverschraubung MS", :dimensions => "3/4-17x2", :price => 8.95},
    {:catalog_number => "51.023601", :description => "Anschlussverschraubung vern.", :dimensions => "3/4-16x2", :price => 7.05},
    {:catalog_number => "", :description => "Verbrauchs- und Kleinmaterial", :dimensions => "", :price => 3},
    {:catalog_number => "aa", :description => "Einsatz Servicewagen", :dimensions => "", :price => 18},
  ])

### TimeEntryTypes
materials = Material.all
t = TimeEntryType.create!(:description => "Heizung ansehen")
t.time_entry_type_materials = [
    TimeEntryTypeMaterial.new(:material => materials[9], :time_entry_type => t),
  ]
t.save
t = TimeEntryType.create!(:description => "Lavabo wechseln")
t.time_entry_type_materials = [
    TimeEntryTypeMaterial.new(:material => materials[0], :time_entry_type => t),
    TimeEntryTypeMaterial.new(:material => materials[1], :time_entry_type => t),
    TimeEntryTypeMaterial.new(:material => materials[4], :time_entry_type => t, :amount => 5),
    TimeEntryTypeMaterial.new(:material => materials[9], :time_entry_type => t),
  ]
t.save
t = TimeEntryType.create!(:description => "Wasserzähler ersetzen")
t.time_entry_type_materials = [
    TimeEntryTypeMaterial.new(:material => materials[8], :time_entry_type => t),
    TimeEntryTypeMaterial.new(:material => materials[9], :time_entry_type => t),
  ]
t.save

### Addresses
Address.create!([
    { :line1 => "Hungerbergstr. 1", :zip => "8046", :place => "Zürich" },
    { :line1 => "Oberseestrasse 10", :zip => "8640", :place => "Rapperswil" },
    { :line1 => "Bundesgasse 3", :zip => "3005", :place => "Bern" },
    { :line1 => "Hungerbergstr. 4", :zip => "8046", :place => "Zürich" },
    { :line1 => "Hungerbergstr. 100", :zip => "8046", :place => "Zürich" },
    { :line1 => "Oberseestrasse 11", :zip => "8640", :place => "Rapperswil" },
    { :line1 => "Bundesgasse 1", :zip => "3005", :place => "Bern" },
    { :line1 => "Hungerbergstr. 40", :zip => "8046", :place => "Zürich" },
  ])

### Customers
addresses = Address.all
"Waldemar Lamprecht, Otto Traugott, Hermann Elmo, Veit Ingolf, Björn Simon,
Klaus Dietfried, Gotthilf Wieland, June Gretchen, Taryn Shelia, Tarah Dione,
Angelica Xavia, Demontongue Catbroom, Silverbeam Blackmoon, Whirl Gnaw, Nike Medb,
Pia Ligeia, Ásdís Eirene, Beatrix Afra, Rochus Rocco, Eirene Felicianus, Hrodger Amantius,
Sigimund Lóegaire, Friðþjófr Ramessu, Yima Philander, Enlil Prabhu, Owain Iovis, Týr Cronus,
Partha Pramoda, Gawain Jarl, Cupid Amulius".split(',').each do |name|
  f, l = name.strip.split(' ')
  Customer.create!(:first_name => f.strip, :last_name => l.strip, :address => addresses[rand(addresses.length)], :phone => "0#{rand(899999999) + 10**8}")
end

### Orders
customers = Customer.all
Order.create!([
    {:customer => customers[0], :address => addresses[0], :description => "Lavabo wechseln"},
    {:customer => customers[1], :address => addresses[1], :description => "WC wechseln"},
    {:customer => customers[2], :address => addresses[2], :description => "Bad wechseln"},
  ])

### TimeEntries
users = User.field_workers
time_entry_types = TimeEntryType.all
orders = Order.all
TimeEntry.create!([
    { :user => users[0], :gps_position => addresses[0].gps_position, :customer => customers[0], :time_entry_type => time_entry_types[0], :order => orders[0], :description => "Lavabo wechseln", :time_start => 10.hours.ago, :time_stop => 8.hours.ago },
    { :user => users[0], :gps_position => addresses[0].gps_position, :customer => customers[0], :time_entry_type => time_entry_types[0], :order => orders[0], :description => "Hanen wechseln", :time_start => 8.hours.ago, :time_stop => 7.hours.ago },
    { :user => users[0], :gps_position => addresses[0].gps_position, :customer => customers[0], :time_entry_type => time_entry_types[0], :order => orders[0], :description => "Siphon wechseln", :time_start => 6.hours.ago, :time_stop => 5.hours.ago },
    { :user => users[1], :gps_position => addresses[1].gps_position, :customer => customers[1], :time_entry_type => time_entry_types[1], :order => orders[1], :description => "Schüssel wechseln", :time_start => 10.hours.ago, :time_stop => 8.hours.ago },
    { :user => users[1], :gps_position => addresses[1].gps_position, :customer => customers[1], :time_entry_type => time_entry_types[1], :order => orders[1], :description => "Spülkasten wechseln", :time_start => 8.hours.ago, :time_stop => 7.hours.ago },
    { :user => users[1], :gps_position => addresses[1].gps_position, :customer => customers[2], :time_entry_type => time_entry_types[2], :order => orders[2], :description => "Wanne wechseln", :time_start => 10.hours.ago, :time_stop => 8.hours.ago },
  ])

# It may be a good idea to have the same test data as the initial data? The use this command to load all fixtures!
# system("rake db:fixtures:load")



